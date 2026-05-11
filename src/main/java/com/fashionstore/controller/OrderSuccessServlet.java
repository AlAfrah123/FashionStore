package com.fashionstore.controller;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/** Read-only confirmation for the most recently completed checkout. */
@WebServlet("/order-success")
public class OrderSuccessServlet extends HttpServlet {

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() {

        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("orderId");

        if (idRaw == null || idRaw.isBlank()) {

            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        int orderId;

        try {

            orderId = Integer.parseInt(idRaw.trim());

        } catch (NumberFormatException e) {

            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        Order order = orderDAO.getOrderById(orderId);

        if (order == null) {

            response.sendRedirect(request.getContextPath() + "/products");

            return;
        }

        List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(orderId);

        request.setAttribute("order", order);
        request.setAttribute("orderItems", items);
        request.setAttribute("activeNav", "products");

        request.getRequestDispatcher("/WEB-INF/views/order-success.jsp").forward(request, response);
    }
}
