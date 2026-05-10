package com.fashionstore.controller;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdParam = request.getParameter("categoryId");
        String query = request.getParameter("query");

        List<Product> products;

        if (query != null && !query.trim().isEmpty()) {
            products = productDAO.getProductsByName(query.trim());

        } else if (categoryIdParam != null && !categoryIdParam.isBlank()) {
            try {
                int categoryId = Integer.parseInt(categoryIdParam.trim());
                products = productDAO.getProductsByCategoryId(categoryId);
            } catch (NumberFormatException e) {
                products = productDAO.getAllProducts();
            }

        } else {
            products = productDAO.getAllProducts();
        }

        request.setAttribute("products", products);
        request.setAttribute("activeCategoryId", categoryIdParam);

        request.getRequestDispatcher("/WEB-INF/views/products.jsp")
               .forward(request, response);
    }
}