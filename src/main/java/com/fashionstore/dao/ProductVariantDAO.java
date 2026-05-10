package com.fashionstore.dao;

import com.fashionstore.model.ProductSize;
import java.util.List;

public interface ProductVariantDAO {

    boolean addProductVariant(ProductSize variant);

    boolean updateProductVariant(ProductSize variant);

    boolean deleteProductVariant(int productSizeId);

    ProductSize getVariantById(int productSizeId);

    List<ProductSize> getVariantsByProductId(int productId);

    List<ProductSize> getVariantsBySize(String sizeLabel);

    List<ProductSize> getAvailableVariantsByProductId(int productId);

    boolean updateStock(int productSizeId, int stockQuantity);

    boolean reduceStock(int productSizeId, int quantity);

    boolean variantExists(int productId, String sizeLabel);
}