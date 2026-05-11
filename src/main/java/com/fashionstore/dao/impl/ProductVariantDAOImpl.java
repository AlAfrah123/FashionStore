package com.fashionstore.dao.impl;

import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.model.ProductSize;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductVariantDAOImpl implements ProductVariantDAO {

    // =========================
    // SQL CONSTANTS
    // =========================

    private static final String INSERT_VARIANT =
            "INSERT INTO product_sizes (product_id, size_label, stock_quantity, sku_code, is_available) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_VARIANT =
            "UPDATE product_sizes SET product_id=?, size_label=?, stock_quantity=?, sku_code=?, is_available=? WHERE product_size_id=?";

    private static final String DELETE_VARIANT =
            "DELETE FROM product_sizes WHERE product_size_id=?";

    private static final String GET_BY_ID =
            "SELECT * FROM product_sizes WHERE product_size_id=?";

    private static final String GET_BY_PRODUCT_ID =
            "SELECT * FROM product_sizes WHERE product_id=?";

    private static final String GET_BY_SIZE =
            "SELECT * FROM product_sizes WHERE size_label=?";

    private static final String GET_AVAILABLE_BY_PRODUCT =
            "SELECT * FROM product_sizes WHERE product_id=? AND is_available=TRUE AND stock_quantity > 0";

    private static final String UPDATE_STOCK =
            "UPDATE product_sizes SET stock_quantity=? WHERE product_size_id=?";

    private static final String REDUCE_STOCK =
            "UPDATE product_sizes SET stock_quantity = stock_quantity - ? WHERE product_size_id=? AND stock_quantity >= ?";

    private static final String INCREMENT_STOCK =
            "UPDATE product_sizes SET stock_quantity = stock_quantity + ? WHERE product_size_id=?";

    private static final String CHECK_VARIANT =
            "SELECT product_size_id FROM product_sizes WHERE product_id=? AND size_label=?";

    // =========================
    // CORE METHODS
    // =========================

    @Override
    public boolean addProductVariant(ProductSize variant) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_VARIANT)) {

            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getSizeLabel());
            ps.setInt(3, variant.getStockQuantity());
            ps.setString(4, variant.getSkuCode());
            ps.setBoolean(5, variant.isAvailable());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateProductVariant(ProductSize variant) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_VARIANT)) {

            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getSizeLabel());
            ps.setInt(3, variant.getStockQuantity());
            ps.setString(4, variant.getSkuCode());
            ps.setBoolean(5, variant.isAvailable());
            ps.setInt(6, variant.getProductSizeId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteProductVariant(int productSizeId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_VARIANT)) {

            ps.setInt(1, productSizeId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public ProductSize getVariantById(int productSizeId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_ID)) {

            ps.setInt(1, productSizeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapVariant(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ProductSize> getVariantsByProductId(int productId) {

        List<ProductSize> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_PRODUCT_ID)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapVariant(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<ProductSize> getVariantsBySize(String sizeLabel) {

        List<ProductSize> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_SIZE)) {

            ps.setString(1, sizeLabel);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapVariant(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<ProductSize> getAvailableVariantsByProductId(int productId) {

        List<ProductSize> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_AVAILABLE_BY_PRODUCT)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapVariant(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean updateStock(int productSizeId, int stockQuantity) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STOCK)) {

            ps.setInt(1, stockQuantity);
            ps.setInt(2, productSizeId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean reduceStock(int productSizeId, int quantity) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(REDUCE_STOCK)) {

            ps.setInt(1, quantity);
            ps.setInt(2, productSizeId);
            ps.setInt(3, quantity);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean incrementStock(int productSizeId, int quantity) {

        if (quantity <= 0) {
            return true;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INCREMENT_STOCK)) {

            ps.setInt(1, quantity);
            ps.setInt(2, productSizeId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean variantExists(int productId, String sizeLabel) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_VARIANT)) {

            ps.setInt(1, productId);
            ps.setString(2, sizeLabel);

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

    private ProductSize mapVariant(ResultSet rs) throws SQLException {

        ProductSize variant = new ProductSize();

        variant.setProductSizeId(rs.getInt("product_size_id"));
        variant.setProductId(rs.getInt("product_id"));
        variant.setSizeLabel(rs.getString("size_label"));
        variant.setStockQuantity(rs.getInt("stock_quantity"));
        variant.setSkuCode(rs.getString("sku_code"));
        variant.setAvailable(rs.getBoolean("is_available"));

        return variant;
    }
}