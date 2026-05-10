package com.fashionstore.dao;

import com.fashionstore.model.Order;
import java.util.List;

public interface OrderDAO {

    int placeOrder(Order order);

    Order getOrderById(int orderId);

    List<Order> getOrdersByUserId(int userId);

    List<Order> getAllOrders();

    boolean updateOrderStatus(int orderId, String status);

    boolean cancelOrder(int orderId);

    boolean deleteOrder(int orderId);

    List<Order> getOrdersByStatus(String status);
}