package com.example.Ecommerce.service;

import com.example.Ecommerce.Exception.ResourceNotFoundException;
import com.example.Ecommerce.model.CartItem;
import com.example.Ecommerce.repo.CartItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepo cartItemRepo;

    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartItemRepo.save(cartItem);
    }

    public List<Integer> getCartItemIdsByCartId(int cartId) {
        return cartItemRepo.findCartItemIdsByCartId(cartId);
    }
}
