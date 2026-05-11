package com.fashionstore.controller;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.ProductVariantDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductSize;
import com.fashionstore.model.ShoppingCartLine;
import com.fashionstore.util.CommerceConstants;
import com.fashionstore.util.SessionCart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Session cart orchestration ({@link SessionCart}); delegates inventory reads
 * to {@link ProductVariantDAO}.
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    public static final String FLASH_SESSION = "cartFlashMessage";

    private ProductDAO productDAO;
    private ProductVariantDAO variantDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
        variantDAO = new ProductVariantDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        migrateFlash(session, request);

        List<ShoppingCartLine> lines = SessionCart.getLines(session);
        double subtotal = SessionCart.subtotal(lines);

        request.setAttribute("cartLines", lines);
        request.setAttribute("cartSubtotal", subtotal);
        request.setAttribute("cartShippingPreview", CommerceConstants.SHIPPING_FLAT_INR);
        request.setAttribute("activeNav", "cart");

        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        if (action == null || action.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        switch (action) {
            case "add" -> handleAdd(request, session);
            case "update_qty" -> handleUpdateQty(request, session);
            case "remove" -> handleRemove(request, session);
            case "clear" -> SessionCart.clear(session);
            default -> session.setAttribute(FLASH_SESSION, "Unknown cart action.");
        }

        String ctx = request.getContextPath();

        response.sendRedirect(ctx + "/cart");
    }

    private static void migrateFlash(HttpSession session, HttpServletRequest request) {

        Object raw = session.getAttribute(FLASH_SESSION);

        if (raw instanceof String s && !s.isBlank()) {
            request.setAttribute("flashMessage", s);
            session.removeAttribute(FLASH_SESSION);
        }
    }

    private void handleAdd(HttpServletRequest request, HttpSession session) {

        String sizeIdParam = request.getParameter("productSizeId");

        String productIdParam = request.getParameter("productId");
        String qtyParam = request.getParameter("qty");

        int productSizeId;

        if (sizeIdParam == null || sizeIdParam.isBlank()) {

            Integer resolved = resolveSingleAutoSize(productIdParam);

            if (resolved == null) {

                session.setAttribute(FLASH_SESSION, "Pick a size before adding to cart.");
                return;
            }

            productSizeId = resolved;

        } else {

            try {
                productSizeId = Integer.parseInt(sizeIdParam.trim());
            } catch (NumberFormatException e) {
                session.setAttribute(FLASH_SESSION, "Invalid size.");
                return;
            }
        }

        int qty = parsePositiveInt(qtyParam, 1);

        ProductSize variant = variantDAO.getVariantById(productSizeId);

        if (variant == null || variant.getStockQuantity() < 1) {
            session.setAttribute(FLASH_SESSION, "Selected size is out of stock.");
            return;
        }

        int pid = variant.getProductId();

        if (productIdParam != null && !productIdParam.isBlank()) {

            try {

                int bodyPid = Integer.parseInt(productIdParam.trim());

                if (bodyPid != pid) {

                    session.setAttribute(FLASH_SESSION, "Variant does not match product.");
                    return;
                }

            } catch (NumberFormatException ignored) {

                session.setAttribute(FLASH_SESSION, "Invalid product reference.");
                return;
            }
        }

        Product product = productDAO.getProductById(pid);

        if (product == null) {
            session.setAttribute(FLASH_SESSION, "Product not found.");
            return;
        }

        List<ShoppingCartLine> cart = SessionCart.getLines(session);
        ShoppingCartLine existing = SessionCart.findLine(cart, productSizeId);

        int currentQtyInCart = existing != null ? existing.getQuantity() : 0;

        int maxUsable = Math.min(variant.getStockQuantity(), currentQtyInCart + qty);

        if (maxUsable <= currentQtyInCart) {
            session.setAttribute(FLASH_SESSION, "Maximum stock reached for this size.");
            return;
        }

        int addQty = maxUsable - currentQtyInCart;

        double unit = SessionCart.round2(product.getFinalPrice());

        if (existing == null) {

            ShoppingCartLine line = new ShoppingCartLine();
            line.setProductSizeId(productSizeId);
            line.setProductId(pid);
            line.setProductName(product.getProductName());
            line.setSizeLabel(variant.getSizeLabel());
            line.setQuantity(addQty);
            line.setUnitPrice(unit);
            line.setImageUrl(product.getImageUrl());

            cart.add(line);

        } else {

            existing.setQuantity(maxUsable);
            existing.setUnitPrice(unit);
        }

        SessionCart.replaceAll(session, cart);
        session.setAttribute(FLASH_SESSION, "Added to cart.");
    }

    /**
     * When the catalog has exactly one in-stock variant, allow add without an
     * explicit radio selection.
     */
    private Integer resolveSingleAutoSize(String productIdParam) {

        if (productIdParam == null || productIdParam.isBlank()) {
            return null;
        }

        int pid;

        try {
            pid = Integer.parseInt(productIdParam.trim());
        } catch (NumberFormatException e) {
            return null;
        }

        List<ProductSize> variants = variantDAO.getVariantsByProductId(pid);

        List<ProductSize> inStock = variants.stream()
                .filter(v -> v.getStockQuantity() > 0)
                .toList();

        if (inStock.size() == 1) {
            return inStock.get(0).getProductSizeId();
        }

        return null;
    }

    private void handleUpdateQty(HttpServletRequest request, HttpSession session) {

        String sizeIdParam = request.getParameter("productSizeId");

        String qtyParam = request.getParameter("qty");

        if (sizeIdParam == null || sizeIdParam.isBlank()) {
            session.setAttribute(FLASH_SESSION, "Missing line.");
            return;
        }

        int productSizeId;

        try {
            productSizeId = Integer.parseInt(sizeIdParam.trim());
        } catch (NumberFormatException e) {
            session.setAttribute(FLASH_SESSION, "Invalid line.");
            return;
        }

        int requested = parsePositiveInt(qtyParam, 1);

        ProductSize variant = variantDAO.getVariantById(productSizeId);

        if (variant == null) {
            session.setAttribute(FLASH_SESSION, "Size no longer exists.");
            return;
        }

        List<ShoppingCartLine> cart = SessionCart.getLines(session);
        ShoppingCartLine existing = SessionCart.findLine(cart, productSizeId);

        if (existing == null) {

            session.setAttribute(FLASH_SESSION, "Cart line missing.");
            return;
        }

        if (requested < 1) {

            SessionCart.removeLineByProductSize(cart, productSizeId);
            SessionCart.replaceAll(session, cart);
            session.setAttribute(FLASH_SESSION, "Item removed.");

            return;
        }

        int capped = Math.min(requested, variant.getStockQuantity());

        if (capped < requested) {
            session.setAttribute(FLASH_SESSION, "Qty reduced to stock on hand (" + capped + ").");
        }

        existing.setQuantity(capped);
        SessionCart.replaceAll(session, cart);
    }

    private void handleRemove(HttpServletRequest request, HttpSession session) {

        String sizeIdParam = request.getParameter("productSizeId");

        if (sizeIdParam == null || sizeIdParam.isBlank()) {
            session.setAttribute(FLASH_SESSION, "Missing line.");
            return;
        }

        int productSizeId;

        try {

            productSizeId = Integer.parseInt(sizeIdParam.trim());

        } catch (NumberFormatException e) {

            session.setAttribute(FLASH_SESSION, "Invalid line.");
            return;
        }

        List<ShoppingCartLine> cart = SessionCart.getLines(session);

        if (SessionCart.removeLineByProductSize(cart, productSizeId)) {

            SessionCart.replaceAll(session, cart);

            session.setAttribute(FLASH_SESSION, "Removed from cart.");

        } else {

            session.setAttribute(FLASH_SESSION, "Cart line missing.");
        }
    }

    private static int parsePositiveInt(String raw, int fallback) {

        if (raw == null || raw.isBlank()) {
            return Math.max(fallback, 1);
        }

        try {

            int v = Integer.parseInt(raw.trim());

            return Math.max(v, 1);

        } catch (NumberFormatException e) {

            return Math.max(fallback, 1);
        }
    }
}
