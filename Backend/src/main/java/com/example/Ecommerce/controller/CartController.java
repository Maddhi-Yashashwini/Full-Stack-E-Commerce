package com.example.Ecommerce.controller;

import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.User;
import com.example.Ecommerce.repo.UserRepo;
import com.example.Ecommerce.service.CartService;
import com.example.Ecommerce.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

@PostMapping("/add/{productId}")
public ResponseEntity<String> addToCart(@PathVariable int productId,
                                        @RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(400).body("Invalid Authorization header!");
    }

    String token = authHeader.substring(7);
    String username = jwtService.extractUsername(token);

    User user = userRepo.findByUsername(username);
    if (user == null) {
        return ResponseEntity.status(404).body("User not found!");
    }

    cartService.addToCart(user.getUserId(), productId);
    return ResponseEntity.ok("Product added to cart");
}


    @GetMapping()
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        try {
            //int userId = (int) request.getAttribute("userId");
            int userId = jwtService.extractUserId(token.substring(7));

            Cart cart = cartService.getCart(userId);
            return ResponseEntity.ok(cart);
        }
        catch(Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch cart: " + e.getMessage());
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable int productId,
            @RequestHeader("Authorization") String token) {
        try {
            int userId = jwtService.extractUserId(token.substring(7));

            cartService.removeFromCart(productId, userId);
            return ResponseEntity.ok("Product removed successfully from the cart.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to remove product from cart: " + e.getMessage());
        }
    }
}


