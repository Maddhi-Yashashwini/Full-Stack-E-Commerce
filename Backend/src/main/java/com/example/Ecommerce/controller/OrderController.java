package com.example.Ecommerce.controller;

import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.CartItem;
import com.example.Ecommerce.model.User;
import com.example.Ecommerce.model.order.Order;
import com.example.Ecommerce.model.order.OrderStatus;
import com.example.Ecommerce.repo.CartItemRepo;
import com.example.Ecommerce.repo.CartRepo;
import com.example.Ecommerce.repo.OrderRepo;
import com.example.Ecommerce.repo.UserRepo;
import com.example.Ecommerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class OrderController {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private JwtService jwtService;

    /**
     * Places an order by converting cart items into orders.
     * Clears the cart after successful checkout.
     */
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepo.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user or token.");
        }

        Cart cart = cartRepo.findByUser(user).orElse(null);
        if (cart == null || cart.getCartItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty.");
        }

        List<Order> orders = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            Order order = new Order();
            order.setUser(user);
            order.setProduct(item.getProduct());
            order.setQuantity(item.getQuantity());
            order.setStatus(OrderStatus.Pending);
            orders.add(order);
        }

        orderRepo.saveAll(orders);
        cartItemRepo.deleteAll(cart.getCartItems());
        cart.setTotalPrice(0.0);
        cart.getCartItems().clear();
        cartRepo.save(cart);

        return ResponseEntity.ok("Order placed successfully.");
    }

    /**
     * Retrieves all orders of the logged-in user.
     */
    @GetMapping("/myOrders")
    public ResponseEntity<?> getUserOrders(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepo.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user or token.");
        }

        List<Order> orders = orderRepo.findByUserOrderByCreatedAtDesc(user);
        return ResponseEntity.ok(orders);
    }
}
