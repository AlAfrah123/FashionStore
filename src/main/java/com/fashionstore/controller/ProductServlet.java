package com.fashionstore.controller;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Category;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductSortOption;

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
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryIdParam = request.getParameter("categoryId");
        String query = request.getParameter("query");
        ProductSortOption sort = ProductSortOption.fromRequestParam(request.getParameter("sort"));

        String currentSortSlug =
                switch (sort) {

                    case POPULARITY -> "popularity";

                    case PRICE_ASC -> "price_asc";

                    case PRICE_DESC -> "price_desc";

                    case NEWEST -> "newest";
                };

        request.setAttribute("currentSort", currentSortSlug);

        Integer categoryIdObj = null;

        if (categoryIdParam != null && !categoryIdParam.isBlank()) {

            try {
                categoryIdObj = Integer.valueOf(categoryIdParam.trim());

            } catch (NumberFormatException e) {

                categoryIdObj = null;
            }
        }

        String trimmedQuery =
                query != null && !query.trim().isEmpty()
                        ? query.trim()
                        : null;

        List<Product> products = productDAO.findProducts(categoryIdObj, trimmedQuery, sort);

        request.setAttribute("products", products);
        request.setAttribute("productCount", products.size());
        request.setAttribute("activeCategoryId", categoryIdParam);

        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.setAttribute("activeNav", "products");

        request.getRequestDispatcher("/WEB-INF/views/products.jsp")
               .forward(request, response);
    }
}