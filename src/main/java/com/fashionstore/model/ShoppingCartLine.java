package com.fashionstore.model;

import java.io.Serializable;

/**
 * Serialized in HTTP session lines for guest checkout.
 * References {@link ProductSize} via productSizeId.
 */
public class ShoppingCartLine implements Serializable {

    private static final long serialVersionUID = 1L;

    private int productSizeId;
    private int productId;
    private String productName;
    private String sizeLabel;
    private int quantity;
    private double unitPrice;
    private String imageUrl;

    public ShoppingCartLine() {}

    public int getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(int productSizeId) {
        this.productSizeId = productSizeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSizeLabel() {
        return sizeLabel;
    }

    public void setSizeLabel(String sizeLabel) {
        this.sizeLabel = sizeLabel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLineTotal() {
        return unitPrice * quantity;
    }
}
