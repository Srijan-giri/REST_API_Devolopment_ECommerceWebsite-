package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exception.BadApiRequestException;
import com.lcwd.electronic.store.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if(quantity<=0)
        {
            throw new BadApiRequestException("Requested quantity is not valid !!");
        }

        // fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in database !!"));

        // fetch the user from db
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found in database !!"));



        Cart cart = null;

        try {

            cart = cartRepository.findByUser(user).get();

        }
        catch(NoSuchElementException e)
        {
            cart = new Cart();
            String cartId = UUID.randomUUID().toString();
            cart.setCartId(cartId);
            cart.setCreatedAt(new Date());
        }



        //perform cart operation

        // if cart items already present then update
        AtomicReference<Boolean> updatedItem = new AtomicReference<>(false);

        List<CartItem> items = cart.getItems();

        List<CartItem> updatedItems = items.stream().map(item -> {

            if (item.getProduct().getProductId().equalsIgnoreCase(productId)) {

                // item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
                updatedItem.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);



        // create items
        if(!updatedItem.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();


            cart.getItems().add(cartItem);
        }

        cart.setUser(user);

        Cart updatedCart = cartRepository.save(cart);

        return modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {


        // conditions

        CartItem cartItem1 = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found  !!"));
        cartItemRepository.delete(cartItem1);

    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found in database !!"));
        Cart cart = cartRepository.findByUser(user).get();

        cart.getItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found in database !!"));
        System.out.println("User found: " + user.getUserId() + ":" + user.getName());

        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found in database for respective user !!"));
        System.out.println("Cart found: " + cart.getCartId());

        List<CartItem> items = cart.getItems();
        if (items != null) {
            System.out.println("Number of items in cart: " + items.size());
            items.forEach(item -> System.out.println("Item: " + item.getProduct().getProductId() + ", Quantity: " + item.getQuantity()));
        } else {
            System.out.println("Cart items are null.");
        }

        CartDto cartDto;
        try {
            cartDto = modelMapper.map(cart, CartDto.class);
        }
        catch(Exception e)
        {
            System.out.println("Error mapping Cart to CartDto: " + e.getMessage());
            throw e;
        }

        return cartDto;

    }
}
