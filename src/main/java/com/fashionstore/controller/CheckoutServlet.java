package com.fashionstore.controller;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.dao.impl.ProductVariantDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.ProductSize;
import com.fashionstore.model.ShoppingCartLine;
import com.fashionstore.model.User;
import com.fashionstore.util.AuthConstants;
import com.fashionstore.util.CommerceConstants;
import com.fashionstore.util.SessionCart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Collects delivery + payment, reserves stock via {@link ProductVariantDAO#reduceStock(int, int)},
 * then persists {@link Order} + {@link OrderItem} rows linked to {@link ProductSize}.
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    static final String FLASH_SESSION = "checkoutFlashMessage";

    /** Used when no authenticated user is stored in session ({@code loggedInUserId}). */
    static final int FALLBACK_GUEST_USER_ID = 1;

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ProductVariantDAO variantDAO;

    @Override
    public void init() {

        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
        variantDAO = new ProductVariantDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        migrateFlash(session, request);

        List<ShoppingCartLine> lines = SessionCart.getLines(session);

        if (lines.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        double subtotal = SessionCart.subtotal(lines);

        request.setAttribute("cartLines", lines);
        request.setAttribute("cartSubtotal", subtotal);
        request.setAttribute("shippingCharge", CommerceConstants.SHIPPING_FLAT_INR);
        request.setAttribute(
                "orderGrandTotal",
                SessionCart.round2(subtotal + CommerceConstants.SHIPPING_FLAT_INR));

        User auth = (User) session.getAttribute(AuthConstants.SESSION_USER);

        if (auth != null
                && auth.getAddress() != null
                && !auth.getAddress().isBlank()) {

            request.setAttribute("prefillAddress", auth.getAddress());
        }

        request.setAttribute("activeNav", "cart");

        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        List<ShoppingCartLine> lines = SessionCart.getLines(session);

        if (lines.isEmpty()) {
            flash(session, "Your cart is empty.");
            response.sendRedirect(request.getContextPath() + "/checkout");
            return;
        }

        String address = request.getParameter("deliveryAddress");
        String payment = request.getParameter("paymentMethod");

        if (address == null || address.trim().length() < 10) {

            flash(session, "Enter a full delivery address (at least 10 characters).");

            response.sendRedirect(request.getContextPath() + "/checkout");

            return;
        }

        if (payment == null || payment.isBlank()) {

            payment = "COD";
        }

        Integer loggedUserId = (Integer) session.getAttribute("loggedInUserId");
        int userId = loggedUserId != null ? loggedUserId : FALLBACK_GUEST_USER_ID;

        double subtotal = SessionCart.subtotal(lines);
        double grandTotal =
                SessionCart.round2(subtotal + CommerceConstants.SHIPPING_FLAT_INR);

        List<StockReservation> reservations = new ArrayList<>();

        for (ShoppingCartLine line : lines) {

            ProductSize variant = variantDAO.getVariantById(line.getProductSizeId());

            if (variant == null) {

                releaseReservations(variantDAO, reservations);

                flash(session, "Cart contains an invalid variant. Please refresh and try again.");

                response.sendRedirect(request.getContextPath() + "/cart");

                return;
            }

            if (variant.getStockQuantity() < line.getQuantity()) {

                releaseReservations(variantDAO, reservations);

                flash(
                        session,

                        "Not enough stock for "
                                + line.getProductName()
                                + " ("
                                + line.getSizeLabel()
                                + "). Available: "
                                + variant.getStockQuantity());

                response.sendRedirect(request.getContextPath() + "/checkout");

                return;
            }

            if (!variantDAO.reduceStock(line.getProductSizeId(), line.getQuantity())) {

                releaseReservations(variantDAO, reservations);

                flash(session, "Stock reservation failed — try again.");

                response.sendRedirect(request.getContextPath() + "/checkout");

                return;
            }

            reservations.add(new StockReservation(line.getProductSizeId(), line.getQuantity()));
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(grandTotal);
        order.setPaymentMethod(payment.trim());
        order.setOrderStatus("PLACED");
        order.setDeliveryAddress(address.trim());

        int orderId = orderDAO.placeOrder(order);

        if (orderId < 1) {

            releaseReservations(variantDAO, reservations);

            flash(session, "Could not save your order.");

            response.sendRedirect(request.getContextPath() + "/checkout");

            return;
        }

        List<OrderItem> payload = buildOrderLines(orderId, lines);

        if (!orderItemDAO.addOrderItems(payload)) {

            orderItemDAO.deleteOrderItemsByOrderId(orderId);
            orderDAO.deleteOrder(orderId);

            releaseReservations(variantDAO, reservations);

            flash(session, "Could not finalize line items.");

            response.sendRedirect(request.getContextPath() + "/checkout");

            return;
        }

        SessionCart.clear(session);

        response.sendRedirect(request.getContextPath() + "/order-success?orderId=" + orderId);
    }

    private List<OrderItem> buildOrderLines(int orderId, List<ShoppingCartLine> lines) {

        List<OrderItem> rows = new ArrayList<>();

        for (ShoppingCartLine line : lines) {

            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setProductId(line.getProductId());
            item.setProductName(line.getProductName());
            item.setQuantity(line.getQuantity());
            item.setUnitPrice(SessionCart.round2(line.getUnitPrice()));
            double subtotal = SessionCart.round2(line.getUnitPrice() * line.getQuantity());
            item.setSubtotal(subtotal);
            item.setSizeLabel(line.getSizeLabel());
            item.setProductSizeId(line.getProductSizeId());

            rows.add(item);
        }

        return rows;
    }

    private static void releaseReservations(ProductVariantDAO variantDAO, List<StockReservation> applied) {

        for (StockReservation slot : applied) {

            variantDAO.incrementStock(slot.productSizeId(), slot.quantity());
        }
    }

    private static void migrateFlash(HttpSession session, HttpServletRequest request) {

        Object raw = session.getAttribute(FLASH_SESSION);

        if (raw instanceof String s && !s.isBlank()) {

            request.setAttribute("flashMessage", s);
            session.removeAttribute(FLASH_SESSION);
        }
    }

    private static void flash(HttpSession session, String message) {

        session.setAttribute(FLASH_SESSION, message);
    }

    private record StockReservation(int productSizeId, int quantity) {}
}
