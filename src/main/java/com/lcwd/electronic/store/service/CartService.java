package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {

    // USER ====> Cart
    // add items to cart
    // case1: cart for user is not available : we will create a cart and add that item
    // case2: cart available add the items to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);


    //remove item from cart
    void removeItemFromCart(String userId,int cartItem);


    // remove all items from cart
    void clearCart(String userId);


    CartDto getCartByUser(String userId);

}
