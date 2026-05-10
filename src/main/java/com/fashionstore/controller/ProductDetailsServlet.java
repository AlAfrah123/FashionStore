package com.fashionstore.controller;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.dao.impl.ProductVariantDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductSize;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/product-details")
public class ProductDetailsServlet extends HttpServlet {

    private ProductDAO productDAO;
    private ProductVariantDAO variantDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
        variantDAO = new ProductVariantDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect("products.jsp");
            return;
        }

        int productId;

        try {
            productId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("products.jsp");
            return;
        }

        Product product = productDAO.getProductById(productId);

        if (product == null) {
            response.sendRedirect("products.jsp");
            return;
        }

        // existing behavior (UNCHANGED)
        request.setAttribute("product", product);

        // OPTIONAL SAFE ADDITION (does not break anything)
        List<ProductSize> variants =
                variantDAO.getAvailableVariantsByProductId(productId);

        request.setAttribute("variants", variants);

        // 🔥 FIXED PATH (OPTION 2 STRUCTURE)
        request.getRequestDispatcher("/views/partials/product-details.jsp")
                .forward(request, response);
    }
}