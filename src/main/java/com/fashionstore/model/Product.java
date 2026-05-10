package com.fashionstore.model;

import java.io.Serializable;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private int productId;
    private int categoryId;
    private String productName;
    private String description;

    private double price;
    private double discountPercent;

    private String imageUrl;
    private boolean active;

    public Product() {}

    // ================= GETTERS / SETTERS =================

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // ================= BUSINESS LOGIC =================

    /**
     * Final selling price after discount.
     * Safe against invalid discount values.
     */
    public double getFinalPrice() {

        if (price <= 0) return 0;

        double validDiscount = Math.min(Math.max(discountPercent, 0), 100);

        return price - (price * validDiscount / 100.0);
    }
}