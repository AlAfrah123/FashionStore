package com.fashionstore.dao.impl;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductSortOption;
import com.fashionstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    private static final String GET_ALL =
            "SELECT product_id, category_id, product_name, description, price, discount_percent, image_url, is_active FROM products";

    private static final String GET_BY_ID =
            "SELECT * FROM products WHERE product_id=?";

    private static final String GET_BY_CATEGORY =
            "SELECT * FROM products WHERE category_id=?";

    private static final String GET_BY_NAME =
            "SELECT * FROM products WHERE product_name LIKE ?";

    private static final String GET_BY_PRICE_RANGE =
            "SELECT * FROM products WHERE price BETWEEN ? AND ?";

    private static final String INSERT_PRODUCT =
            "INSERT INTO products (category_id, product_name, description, price, discount_percent, image_url, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_PRODUCT =
            "UPDATE products SET category_id=?, product_name=?, description=?, price=?, discount_percent=?, image_url=?, is_active=? WHERE product_id=?";

    private static final String DELETE_PRODUCT =
            "DELETE FROM products WHERE product_id=?";

    private static final String EXISTS_BY_ID =
            "SELECT 1 FROM products WHERE product_id=? LIMIT 1";

    private static final String GET_ACTIVE =
            "SELECT * FROM products WHERE is_active = TRUE";

    private static final String GET_DISCOUNTED =
            "SELECT * FROM products WHERE discount_percent > 0 ORDER BY discount_percent DESC";

    /** Mirrors {@link Product#getFinalPrice()} in SQL for ORDER BY sorting. */
    private static final String EFFECTIVE_PRICE_SQL =
            "(price - (price * LEAST(GREATEST(COALESCE(discount_percent,0),0),100) / 100.0))";

    @Override
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching products", e);
        }

        return products;
    }

    @Override
    public Product getProductById(int id) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapProduct(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching product by id", e);
        }

        return null;
    }

    @Override
    public boolean productExists(int productId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(EXISTS_BY_ID)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking product existence", e);
        }
    }

    @Override
    public List<Product> getProductsByCategoryId(int categoryId) {

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_CATEGORY)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching by category", e);
        }

        return list;
    }

    @Override
    public List<Product> getProductsByName(String name) {

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_NAME)) {

            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error searching products", e);
        }

        return list;
    }

    @Override
    public List<Product> getActiveProducts() {

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ACTIVE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching active products", e);
        }

        return list;
    }

    @Override
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_PRICE_RANGE)) {

            ps.setDouble(1, minPrice);
            ps.setDouble(2, maxPrice);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching by price range", e);
        }

        return list;
    }

    @Override
    public List<Product> getDiscountedProducts() {

        List<Product> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_DISCOUNTED);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching discounted products", e);
        }

        return list;
    }

    @Override
    public List<Product> findProducts(Integer categoryId, String nameQuery, ProductSortOption sortOption) {

        StringBuilder sql = new StringBuilder(
                "SELECT product_id, category_id, product_name, description, price, discount_percent, image_url, is_active "
                        + "FROM products WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (categoryId != null) {
            sql.append(" AND category_id=? ");
            params.add(categoryId);
        }

        if (nameQuery != null && !nameQuery.isBlank()) {
            sql.append(" AND product_name LIKE ? ");
            params.add("%" + nameQuery.trim() + "%");
        }

        sql.append(sortClause(sortOption == null ? ProductSortOption.POPULARITY : sortOption));

        List<Product> results = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    results.add(mapProduct(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error querying products with filters/sort", e);
        }

        return results;
    }

    private String sortClause(ProductSortOption sort) {

        return switch (sort) {
            case PRICE_ASC -> " ORDER BY " + EFFECTIVE_PRICE_SQL + " ASC, product_id ASC ";
            case PRICE_DESC -> " ORDER BY " + EFFECTIVE_PRICE_SQL + " DESC, product_id DESC ";
            case NEWEST -> " ORDER BY product_id DESC ";
            case POPULARITY ->
                    " ORDER BY discount_percent DESC, product_id DESC ";
        };
    }

    @Override
    public boolean addProduct(Product product) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_PRODUCT)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, round2(product.getPrice()));
            ps.setDouble(5, round2(product.getDiscountPercent()));
            ps.setString(6, product.getImageUrl());
            ps.setBoolean(7, product.isActive());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error adding product", e);
        }
    }

    @Override
    public boolean updateProduct(Product product) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_PRODUCT)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, round2(product.getPrice()));
            ps.setDouble(5, round2(product.getDiscountPercent()));
            ps.setString(6, product.getImageUrl());
            ps.setBoolean(7, product.isActive());
            ps.setInt(8, product.getProductId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    @Override
    public boolean deleteProduct(int productId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_PRODUCT)) {

            ps.setInt(1, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    // =========================
    // SAFE MAPPER (FIX HERE)
    // =========================

    private Product mapProduct(ResultSet rs) throws SQLException {

        Product p = new Product();

        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setProductName(rs.getString("product_name"));
        p.setDescription(rs.getString("description"));

        // 🔥 FIX: eliminate floating-point garbage
        p.setPrice(round2(rs.getDouble("price")));
        p.setDiscountPercent(round2(rs.getDouble("discount_percent")));

        p.setImageUrl(rs.getString("image_url"));
        p.setActive(rs.getBoolean("is_active"));

        return p;
    }

    // =========================
    // UTILITY: SAFE ROUNDING
    // =========================

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}