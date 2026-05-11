package com.fashionstore.util;

import com.fashionstore.dao.*;
import com.fashionstore.dao.impl.*;
import com.fashionstore.model.*;

import java.util.List;

public class DaoLayerTestApp {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAOImpl();
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        ProductVariantDAO productVariantDAO = new ProductVariantDAOImpl();
        CartDAO cartDAO = new CartDAOImpl();
        CartItemDAO cartItemDAO = new CartItemDAOImpl();
        OrderDAO orderDAO = new OrderDAOImpl();
        OrderItemDAO orderItemDAO = new OrderItemDAOImpl();

        System.out.println("===== DAO LAYER TEST START =====");

        // =========================
        // USER TEST
        // =========================
        System.out.println("\n--- USER ---");
        System.out.println(userDAO.getUserById(1));

        // =========================
        // CATEGORY TEST
        // =========================
        System.out.println("\n--- CATEGORY ---");
        System.out.println(categoryDAO.getCategoryById(1));

        // =========================
        // PRODUCT TEST
        // =========================
        System.out.println("\n--- PRODUCTS (RAW LIST) ---");
        List<Product> products = productDAO.getAllProducts();
        System.out.println(products);

        System.out.println("\n--- PRODUCTS BY CATEGORY ---");
        System.out.println(productDAO.getProductsByCategoryId(1));

        // =========================
        // PRODUCT VARIANT TEST
        // =========================
        System.out.println("\n--- PRODUCT SIZES ---");
        System.out.println(productVariantDAO.getVariantsByProductId(1));

        // =========================
        // CART TEST
        // =========================
        System.out.println("\n--- CART ---");
        Cart cart = cartDAO.getOrCreateCartByUserId(1);

        if (cart == null) {
            System.out.println("Cart not found for user 1");
            return;
        }

        System.out.println(cart);

        // =========================
        // ADD CART ITEM
        // =========================
        System.out.println("\n--- ADD CART ITEM ---");

        CartItem item = new CartItem();
        item.setCartId(cart.getCartId());
        item.setProductId(1);
        item.setSizeLabel("M");
        item.setQuantity(2);
        item.setUnitPrice(799.00);

        boolean added = cartItemDAO.addCartItem(item);
        System.out.println("Cart Item Added: " + added);

        // =========================
        // FETCH CART ITEMS
        // =========================
        System.out.println("\n--- CART ITEMS ---");
        List<CartItem> cartItems = cartItemDAO.getCartItemsByCartId(cart.getCartId());
        System.out.println(cartItems);

        // =========================
        // CREATE ORDER
        // =========================
        System.out.println("\n--- CREATE ORDER ---");

        Order order = new Order();
        order.setUserId(1);
        order.setTotalAmount(1598.00);
        order.setPaymentMethod("COD");
        order.setOrderStatus("PLACED");
        order.setDeliveryAddress("Bangalore");

        int orderId = orderDAO.placeOrder(order);
        System.out.println("Order ID: " + orderId);

        // =========================
        // ORDER ITEMS INSERT
        // =========================
        System.out.println("\n--- ORDER ITEMS INSERT ---");

        for (CartItem ci : cartItems) {

            OrderItem oi = new OrderItem();
            oi.setOrderId(orderId);
            oi.setProductId(ci.getProductId());
            oi.setProductName("Test Product");
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getUnitPrice());
            oi.setSubtotal(ci.getQuantity() * ci.getUnitPrice());
            oi.setSizeLabel(ci.getSizeLabel());
            oi.setProductSizeId(1);

            orderItemDAO.addOrderItem(oi);
        }

        // =========================
        // VERIFY ORDER ITEMS
        // =========================
        System.out.println("\n--- ORDER ITEMS ---");
        System.out.println(orderItemDAO.getOrderItemsByOrderId(orderId));

        // =========================
        // CLEAR CART
        // =========================
        System.out.println("\n--- CLEAR CART ---");
        boolean cleared = cartItemDAO.clearCartItems(cart.getCartId());
        System.out.println("Cart Cleared: " + cleared);

        System.out.println("\n===== DAO TEST COMPLETED SUCCESSFULLY =====");
    }
}