package com.fashionstore.dao.impl;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.model.Category;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    // =========================
    // SQL CONSTANTS
    // =========================

    private static final String INSERT_CATEGORY =
            "INSERT INTO categories (category_name, description, is_active) VALUES (?, ?, ?)";

    private static final String UPDATE_CATEGORY =
            "UPDATE categories SET category_name=?, description=?, is_active=? WHERE category_id=?";

    private static final String DELETE_CATEGORY =
            "DELETE FROM categories WHERE category_id=?";

    private static final String GET_BY_ID =
            "SELECT * FROM categories WHERE category_id=?";

    private static final String GET_BY_NAME =
            "SELECT * FROM categories WHERE category_name=?";

    private static final String GET_ALL =
            "SELECT * FROM categories";

    private static final String GET_ACTIVE =
            "SELECT * FROM categories WHERE is_active=TRUE";

    private static final String EXISTS =
            "SELECT category_id FROM categories WHERE category_name=?";

    // =========================
    // CORE METHODS
    // =========================

    @Override
    public boolean addCategory(Category category) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_CATEGORY)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.isActive());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateCategory(Category category) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_CATEGORY)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.isActive());
            ps.setInt(4, category.getCategoryId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteCategory(int categoryId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_CATEGORY)) {

            ps.setInt(1, categoryId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Category getCategoryById(int categoryId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_ID)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCategory(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Category getCategoryByName(String categoryName) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_NAME)) {

            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCategory(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Category> getAllCategories() {

        List<Category> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapCategory(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Category> getActiveCategories() {

        List<Category> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ACTIVE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapCategory(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean categoryExists(String categoryName) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(EXISTS)) {

            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // MAPPER (CENTRALIZED TRANSFORMATION)
    // =========================

    private Category mapCategory(ResultSet rs) throws SQLException {

        Category category = new Category();

        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        category.setActive(rs.getBoolean("is_active"));

        return category;
    }
}