package com.fashionstore.dao;

import com.fashionstore.model.OrderItem;
import java.util.List;

public interface OrderItemDAO {

    boolean addOrderItem(OrderItem orderItem);

    boolean addOrderItems(List<OrderItem> orderItems);

    OrderItem getOrderItemById(int orderItemId);

    List<OrderItem> getOrderItemsByOrderId(int orderId);

    List<OrderItem> getOrderItemsByProductId(int productId);

    boolean deleteOrderItem(int orderItemId);

    boolean deleteOrderItemsByOrderId(int orderId);
}