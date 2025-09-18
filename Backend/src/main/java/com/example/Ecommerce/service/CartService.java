package com.example.Ecommerce.service;

import com.example.Ecommerce.Exception.ResourceNotFoundException;
import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.CartItem;
import com.example.Ecommerce.model.Product;
import com.example.Ecommerce.model.User;
import com.example.Ecommerce.repo.CartItemRepo;
import com.example.Ecommerce.repo.CartRepo;
import com.example.Ecommerce.repo.ProductRepo;
import com.example.Ecommerce.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;

    public void createCart(Cart cart) {
        cartRepo.save(cart);
    }

    public void addToCart(int userId, int productId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepo.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepo.save(newCart);
        });

        CartItem cartItem = cartItemRepo.findByCartAndProduct(cart, product).orElseGet(() -> {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setDiscountPrice(product.getDisPrice());
            newCartItem.setQuantity(0);
            return newCartItem;
        });

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItemRepo.save(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() + product.getDisPrice());
        cartRepo.save(cart);
    }

    public Cart getCart(int userId) {
        return cartRepo.findByUserId(userId);
    }

    public Cart removeFromCart(int productId, int userId) {
        Cart cart = cartRepo.findByUserId(userId);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId()==(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product not found in cart"));
        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getDiscountPrice());
        cart.getCartItems().remove(cartItem);
        cartRepo.save(cart);

        return cart;


    }


}
