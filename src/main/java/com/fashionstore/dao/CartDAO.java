package com.fashionstore.dao;

import com.fashionstore.model.Cart;

public interface CartDAO {

    boolean createCart(int userId);

    Cart getCartByUserId(int userId);

    Cart getCartById(int cartId);

    Cart getOrCreateCartByUserId(int userId);

    boolean deleteCart(int cartId);

    boolean cartExists(int userId);
}