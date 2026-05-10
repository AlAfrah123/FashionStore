package com.fashionstore.dao;

import com.fashionstore.model.Product;
import java.util.List;

public interface ProductDAO {

    boolean addProduct(Product product);

    boolean updateProduct(Product product);

    boolean deleteProduct(int productId);

    Product getProductById(int productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategoryId(int categoryId);

    List<Product> getActiveProducts();

    List<Product> getProductsByName(String productName);

    List<Product> getProductsByPriceRange(double minPrice, double maxPrice);

    List<Product> getDiscountedProducts();

    boolean productExists(int productId);
}