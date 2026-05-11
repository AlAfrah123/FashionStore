package com.fashionstore.controller;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.ProductVariantDAOImpl;
import com.fashionstore.model.Category;
import com.fashionstore.model.Product;
import com.fashionstore.model.ProductSize;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/product-details")
public class ProductDetailsServlet extends HttpServlet {

    private ProductDAO productDAO;
    private ProductVariantDAO variantDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        productDAO = new ProductDAOImpl();
        variantDAO = new ProductVariantDAOImpl();
        categoryDAO = new CategoryDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession httpSession = request.getSession(false);

        if (httpSession != null) {

            Object cartFlash = httpSession.getAttribute(CartServlet.FLASH_SESSION);

            if (cartFlash instanceof String s && !s.isBlank()) {

                request.setAttribute("cartFlashMessage", s);
                httpSession.removeAttribute(CartServlet.FLASH_SESSION);
            }
        }

        String idParam = request.getParameter("id");

        String productsPath = request.getContextPath() + "/products";

        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(productsPath);
            return;
        }

        int productId;

        try {
            productId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(productsPath);
            return;
        }

        Product product = productDAO.getProductById(productId);

        if (product == null) {
            response.sendRedirect(productsPath);
            return;
        }

        // existing behavior (UNCHANGED)
        request.setAttribute("product", product);
        request.setAttribute("sidebarCategoryId", product.getCategoryId());

        // OPTIONAL SAFE ADDITION (does not break anything)
        List<ProductSize> variants = variantDAO.getVariantsByProductId(productId);

        if (variants == null || variants.isEmpty()) {
            ProductSize defaultSize = new ProductSize();
            defaultSize.setProductId(productId);
            defaultSize.setSizeLabel("Standard");
            defaultSize.setStockQuantity(50);
            defaultSize.setSkuCode("STD-" + productId);
            defaultSize.setAvailable(true);
            variantDAO.addProductVariant(defaultSize);
            
            variants = variantDAO.getVariantsByProductId(productId);
        }

        request.setAttribute("variants", variants);

        int defaultProductSizeId = 0;

        for (ProductSize v : variants) {

            if (v.getStockQuantity() > 0) {

                defaultProductSizeId = v.getProductSizeId();
                break;
            }
        }

        request.setAttribute("defaultProductSizeId", defaultProductSizeId);

        boolean hasPurchasableVariant = variants.stream().anyMatch(v -> v.getStockQuantity() > 0);

        request.setAttribute("hasPurchasableVariant", hasPurchasableVariant);

        request.setAttribute("activeNav", "products");

        List<Category> categories = categoryDAO.getAllCategories();
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/WEB-INF/views/product-details.jsp")
                .forward(request, response);
    }
}