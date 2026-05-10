package com.fashionstore.dao.impl;

import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.model.CartItem;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAOImpl implements CartItemDAO {

    // =========================
    // SQL CONSTANTS
    // =========================

    private static final String INSERT_ITEM =
            "INSERT INTO cart_items (cart_id, product_id, size_label, quantity, unit_price) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_ITEM =
            "UPDATE cart_items SET quantity=?, unit_price=? WHERE cart_item_id=?";

    private static final String DELETE_ITEM =
            "DELETE FROM cart_items WHERE cart_item_id=?";

    private static final String DELETE_BY_PRODUCT_SIZE =
            "DELETE FROM cart_items WHERE cart_id=? AND product_id=? AND size_label=?";

    private static final String GET_BY_ID =
            "SELECT * FROM cart_items WHERE cart_item_id=?";

    private static final String GET_BY_CART_PRODUCT_SIZE =
            "SELECT * FROM cart_items WHERE cart_id=? AND product_id=? AND size_label=?";

    private static final String GET_BY_CART_ID =
            "SELECT * FROM cart_items WHERE cart_id=?";

    private static final String CLEAR_CART =
            "DELETE FROM cart_items WHERE cart_id=?";

    private static final String UPDATE_QUANTITY =
            "UPDATE cart_items SET quantity=? WHERE cart_item_id=?";

    private static final String CHECK_EXISTS =
            "SELECT cart_item_id FROM cart_items WHERE cart_id=? AND product_id=? AND size_label=?";

    // =========================
    // CORE METHODS
    // =========================

    @Override
    public boolean addCartItem(CartItem cartItem) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ITEM)) {

            ps.setInt(1, cartItem.getCartId());
            ps.setInt(2, cartItem.getProductId());
            ps.setString(3, cartItem.getSizeLabel());
            ps.setInt(4, cartItem.getQuantity());
            ps.setDouble(5, cartItem.getUnitPrice());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateCartItem(CartItem cartItem) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_ITEM)) {

            ps.setInt(1, cartItem.getQuantity());
            ps.setDouble(2, cartItem.getUnitPrice());
            ps.setInt(3, cartItem.getCartItemId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean removeCartItem(int cartItemId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ITEM)) {

            ps.setInt(1, cartItemId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean removeCartItemByProductSize(int cartId, int productId, String sizeLabel) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_PRODUCT_SIZE)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setString(3, sizeLabel);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public CartItem getCartItemById(int cartItemId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_ID)) {

            ps.setInt(1, cartItemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCartItem(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public CartItem getCartItem(int cartId, int productId, String sizeLabel) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_CART_PRODUCT_SIZE)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setString(3, sizeLabel);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCartItem(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CartItem> getCartItemsByCartId(int cartId) {

        List<CartItem> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_CART_ID)) {

            ps.setInt(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCartItem(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean clearCartItems(int cartId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CLEAR_CART)) {

            ps.setInt(1, cartId);

            return ps.executeUpdate() >= 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateQuantity(int cartItemId, int quantity) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_QUANTITY)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean itemExists(int cartId, int productId, String sizeLabel) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_EXISTS)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setString(3, sizeLabel);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // MAPPER
    // =========================

    private CartItem mapCartItem(ResultSet rs) throws java.sql.SQLException {

        CartItem item = new CartItem();

        item.setCartItemId(rs.getInt("cart_item_id"));
        item.setCartId(rs.getInt("cart_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setSizeLabel(rs.getString("size_label"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getDouble("unit_price"));

        return item;
    }
}