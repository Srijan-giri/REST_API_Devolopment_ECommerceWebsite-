package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.service.CartService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // add items to cart

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(
            @PathVariable("userId") String userId,
            @RequestBody AddItemToCartRequest request
    ){
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    //remove item from cart

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @PathVariable("userId") String userId,
            @PathVariable("itemId") int cartItemId
    )
    {
        cartService.removeItemFromCart(userId, cartItemId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Item is removed !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(
            @PathVariable("userId") String userId
    )
    {
        cartService.clearCart(userId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Cart is cleared !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);

    }


    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable("userId") String userId
    )
    {
        CartDto cartByUser = cartService.getCartByUser(userId);

        return new ResponseEntity<>(cartByUser,HttpStatus.OK);

    }

}
