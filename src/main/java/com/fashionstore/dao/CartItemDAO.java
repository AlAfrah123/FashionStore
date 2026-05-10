package com.fashionstore.dao;

import com.fashionstore.model.CartItem;
import java.util.List;

public interface CartItemDAO {

    boolean addCartItem(CartItem cartItem);

    boolean updateCartItem(CartItem cartItem);

    boolean removeCartItem(int cartItemId);

    boolean removeCartItemByProductSize(int cartId, int productId, String sizeLabel);

    CartItem getCartItemById(int cartItemId);

    CartItem getCartItem(int cartId, int productId, String sizeLabel);

    List<CartItem> getCartItemsByCartId(int cartId);

    boolean clearCartItems(int cartId);

    boolean updateQuantity(int cartItemId, int quantity);

    boolean itemExists(int cartId, int productId, String sizeLabel);
}