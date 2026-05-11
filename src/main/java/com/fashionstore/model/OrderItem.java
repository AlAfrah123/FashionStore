package com.fashionstore.model;

public class OrderItem {

    private int orderItemId;
    private int orderId;
    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double subtotal;
    private String sizeLabel;
    /** FK to {@link ProductSize#productSizeId}; required for relational integrity with variants. */
    private int productSizeId;

    public OrderItem() {}

    public OrderItem(int orderItemId, int orderId, int productId,
                     String productName, int quantity,
                     double unitPrice, double subtotal,
                     String sizeLabel, int productSizeId) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.sizeLabel = sizeLabel;
        this.productSizeId = productSizeId;
    }

    /** Legacy constructor mapping to {@link #productSizeId} unset (null in DB layer). */
    public OrderItem(int orderItemId, int orderId, int productId,
                     String productName, int quantity,
                     double unitPrice, double subtotal,
                     String sizeLabel) {

        this(orderItemId, orderId, productId, productName, quantity, unitPrice, subtotal, sizeLabel, 0);
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getSizeLabel() {
        return sizeLabel;
    }

    public void setSizeLabel(String sizeLabel) {
        this.sizeLabel = sizeLabel;
    }

    public int getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(int productSizeId) {
        this.productSizeId = productSizeId;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", sizeLabel='" + sizeLabel + '\'' +
                ", productSizeId=" + productSizeId +
                '}';
    }
}