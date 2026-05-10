package com.fashionstore.controller;

import java.io.IOException;
import java.util.List;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.ProductDAO;

import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;

import com.fashionstore.model.Category;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {

        // INITIALIZE DAO OBJECTS
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // FETCH PRODUCTS
        List<Product> products = productDAO.getAllProducts();

        // FETCH CATEGORIES
        List<Category> categories = categoryDAO.getAllCategories();

        // SEND DATA TO JSP
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);

        // FORWARD TO HOME PAGE
        request.getRequestDispatcher("/WEB-INF/views/home.jsp")
               .forward(request, response);
    }
}