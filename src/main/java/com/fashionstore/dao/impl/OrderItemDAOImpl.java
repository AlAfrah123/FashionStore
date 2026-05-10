package com.fashionstore.dao.impl;

import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.model.OrderItem;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO {

    // =========================
    // SQL CONSTANTS
    // =========================

    private static final String INSERT_ITEM =
            "INSERT INTO order_items (order_id, product_id, product_name, quantity, unit_price, subtotal, size_label) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
            "SELECT * FROM order_items WHERE order_item_id=?";

    private static final String SELECT_BY_ORDER =
            "SELECT * FROM order_items WHERE order_id=?";

    private static final String SELECT_BY_PRODUCT =
            "SELECT * FROM order_items WHERE product_id=?";

    private static final String DELETE_BY_ID =
            "DELETE FROM order_items WHERE order_item_id=?";

    private static final String DELETE_BY_ORDER =
            "DELETE FROM order_items WHERE order_id=?";

    // =========================
    // CORE METHODS
    // =========================

    @Override
    public boolean addOrderItem(OrderItem item) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ITEM)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setString(3, item.getProductName());
            ps.setInt(4, item.getQuantity());
            ps.setDouble(5, item.getUnitPrice());
            ps.setDouble(6, item.getSubtotal());
            ps.setString(7, item.getSizeLabel());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Batch insert (important for cart → order conversion)
    @Override
    public boolean addOrderItems(List<OrderItem> items) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ITEM)) {

            conn.setAutoCommit(false);

            for (OrderItem item : items) {

                ps.setInt(1, item.getOrderId());
                ps.setInt(2, item.getProductId());
                ps.setString(3, item.getProductName());
                ps.setInt(4, item.getQuantity());
                ps.setDouble(5, item.getUnitPrice());
                ps.setDouble(6, item.getSubtotal());
                ps.setString(7, item.getSizeLabel());

                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public OrderItem getOrderItemById(int orderItemId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, orderItemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapOrderItem(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {

        List<OrderItem> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ORDER)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrderItem(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<OrderItem> getOrderItemsByProductId(int productId) {

        List<OrderItem> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_PRODUCT)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrderItem(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean deleteOrderItem(int orderItemId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_ID)) {

            ps.setInt(1, orderItemId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteOrderItemsByOrderId(int orderId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_ORDER)) {

            ps.setInt(1, orderId);

            return ps.executeUpdate() >= 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // MAPPER
    // =========================

    private OrderItem mapOrderItem(ResultSet rs) throws java.sql.SQLException {

        OrderItem item = new OrderItem();

        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getDouble("unit_price"));
        item.setSubtotal(rs.getDouble("subtotal"));
        item.setSizeLabel(rs.getString("size_label"));

        return item;
    }
}