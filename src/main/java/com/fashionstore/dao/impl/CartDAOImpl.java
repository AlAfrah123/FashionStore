package com.fashionstore.dao.impl;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.model.Cart;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDAOImpl implements CartDAO {

    // =========================
    // SQL CONSTANTS (FIXED: cart NOT carts)
    // =========================

    private static final String CREATE_CART =
            "INSERT INTO cart (user_id) VALUES (?)";

    private static final String GET_BY_USER_ID =
            "SELECT * FROM cart WHERE user_id=?";

    private static final String GET_BY_CART_ID =
            "SELECT * FROM cart WHERE cart_id=?";

    private static final String DELETE_CART =
            "DELETE FROM cart WHERE cart_id=?";

    private static final String CHECK_CART_EXISTS =
            "SELECT cart_id FROM cart WHERE user_id=?";

    // =========================
    // CORE METHODS
    // =========================

    @Override
    public boolean createCart(int userId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_CART)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Cart getCartByUserId(int userId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_USER_ID)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCart(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Cart getCartById(int cartId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_CART_ID)) {

            ps.setInt(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCart(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Cart getOrCreateCartByUserId(int userId) {

        Cart cart = getCartByUserId(userId);

        if (cart != null) {
            return cart;
        }

        boolean created = createCart(userId);

        if (created) {
            return getCartByUserId(userId);
        }

        return null;
    }

    @Override
    public boolean deleteCart(int cartId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_CART)) {

            ps.setInt(1, cartId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean cartExists(int userId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_CART_EXISTS)) {

            ps.setInt(1, userId);

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

    private Cart mapCart(ResultSet rs) throws SQLException {

        Cart cart = new Cart();

        cart.setCartId(rs.getInt("cart_id"));
        cart.setUserId(rs.getInt("user_id"));

        return cart;
    }
}