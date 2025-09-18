package com.example.Ecommerce.controller;

import com.example.Ecommerce.config.OrderStatusRequest;
import com.example.Ecommerce.config.ResponseMessage;
import com.example.Ecommerce.model.Category;
import com.example.Ecommerce.model.Product;
import com.example.Ecommerce.model.SubCategory;
import com.example.Ecommerce.model.order.Order;
import com.example.Ecommerce.model.order.OrderStatus;
import com.example.Ecommerce.model.User;
import com.example.Ecommerce.repo.*;
import com.example.Ecommerce.service.ImageService;
import com.example.Ecommerce.service.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/merchant")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class MerchantController {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private SubCategoryRepo subCategoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ImageService imageService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @PostMapping("/addproduct")
    public ResponseEntity<ResponseMessage> uploadMultipleImages(
            @RequestHeader("Authorization") String token,
            @RequestParam("category_name") String categoryName,
            @RequestParam("subCategory_name") String subCategoryName,
            @RequestParam("brand") String brand,
            @RequestParam("description") String description,
            @RequestParam("cost") int cost,
            @RequestParam("discount") int discount,
            @RequestParam("discount_price") int discountPrice,
            @RequestParam("stock") int stock,
            @RequestParam("images") MultipartFile[] files) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body(new ResponseMessage("Invalid Authorization header!"));
        }

        String token1 = token.substring(7);
        String username = jwtService.extractUsername(token1);
        int userId = jwtService.extractUserId(token1);

        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body(new ResponseMessage("User not found!"));
        }

        try {
            Category category = categoryRepo.findByName(categoryName)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName));

            SubCategory subCategory = subCategoryRepo.findBySubCategoryName(subCategoryName)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + subCategoryName));

            List<String> filenames = imageService.uploadImages(files);

            Product product = new Product();
            product.setCategoryId(category);
            product.setSubCategoryId(subCategory);
            product.setName(brand);
            product.setDescription(description);
            product.setPrice(cost);
            product.setDiscount(discount);
            product.setDisPrice(discountPrice);
            product.setStock(stock);
            product.setMerchantId(userId);

            if (!filenames.isEmpty()) {
                product.setImage1(filenames.size() > 0 ? filenames.get(0) : null);
                product.setImage2(filenames.size() > 1 ? filenames.get(1) : null);
                product.setImage3(filenames.size() > 2 ? filenames.get(2) : null);
                product.setImage4(filenames.size() > 3 ? filenames.get(3) : null);
            }

            productRepo.save(product);


            return ResponseEntity.ok(new ResponseMessage("Product added successfully!"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ResponseMessage("Image upload failed: " + e.getMessage()));
        }
    }

//

    @GetMapping("/vieworders")
    public ResponseEntity<List<Order>> viewOrders(
            @RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(404).body(null);
        }

        String token1 = token.substring(7);
        String username = jwtService.extractUsername(token1);
        int userId = jwtService.extractUserId(token1);

        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body(null);
        }

        List<Order> orders = orderRepo.findByProduct_MerchantId((long) user.getUserId());
        for (Order order : orders) {

            order.getProduct().setImage1("http://localhost:8080/api/images/products/" + order.getProduct().getImage1());
        }
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/customerdetails")
    public ResponseEntity<List<User>> getCustomerDetails(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body(null);
        }

        String token1 = token.substring(7);
        String username = jwtService.extractUsername(token1);
        User merchant = userRepo.findByUsername(username);
        if (merchant == null) {
            return ResponseEntity.status(404).body(null);
        }

        List<Order> orders = orderRepo.findByProduct_MerchantId((long) merchant.getUserId());
        List<User> customers = new ArrayList<>();
        for (Order order : orders) {
            User customer = order.getUser();
            if (!customers.contains(customer)) {
                customers.add(customer);
            }
        }

        return ResponseEntity.ok(customers);
    }

    @PostMapping("/updateorderstatus")
    public ResponseEntity<ResponseMessage> updateOrderStatus(
            @RequestHeader("Authorization") String token,
            @RequestBody OrderStatusRequest request) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body(new ResponseMessage("Invalid Authorization header!"));
        }

        String token1 = token.substring(7);
        String username = jwtService.extractUsername(token1);
        int userId = jwtService.extractUserId(token1);

        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body(new ResponseMessage("User not found!"));
        }

        User merchant = userRepo.findByUsername(username);

        if (merchant == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Invalid token or user."));
        }


        int orderId = request.orderId();
        String status = request.status();
        System.out.println(status);

        Order order = orderRepo.findById((int) orderId)
                .orElse(null);


        if (order == null || order.getProduct().getMerchantId() != merchant.getUserId()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Order not found or does not belong to the merchant."));
        }
        System.out.println(order.getProduct().getMerchantId()+" "+ merchant.getUserId());
        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Invalid order status."));
        }

        order.setStatus(orderStatus);
        orderRepo.save(order);

        return ResponseEntity.ok(new ResponseMessage("Order status updated successfully."));
    }


    @Transactional
    @DeleteMapping("/deleteproduct")
    public ResponseEntity<ResponseMessage> deleteProduct(
            @RequestHeader("Authorization") String token,
            @RequestParam int productId) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body(new ResponseMessage("Invalid Authorization header!"));
        }

        String token1 = token.substring(7);
        String username = jwtService.extractUsername(token1);
        int userId = jwtService.extractUserId(token1);

        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body(new ResponseMessage("User not found!"));
        }


        Product product = productRepo.findById(Math.toIntExact(productId))
                .orElse(null);

        if (product == null || (product.getMerchantId()!=(user.getUserId()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Product not found or does not belong to the merchant."));
        }

        productRepo.delete(product);
        return ResponseEntity.ok(new ResponseMessage("Product deleted successfully."));
    }

    @PostMapping("/updateproduct")
    public ResponseEntity<String> updateProduct(@RequestBody Product updatedProduct, @RequestHeader("Authorization") String token) {
        // Extract token validation logic (Assuming you handle JWT authentication)
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No token provided");
        }

        // Fetch existing product from DB
        Product existingProduct = productRepo.findByProductId(updatedProduct.getProductId());
        if (existingProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        // Update product details
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDiscount(updatedProduct.getDiscount());
        existingProduct.setDisPrice(updatedProduct.getDisPrice());


        // Fetch and set category if provided
        if (updatedProduct.getCategoryId() != null) {
            Category category = categoryRepo.findById(updatedProduct.getCategoryId().getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
            existingProduct.setCategoryId(category);
        }

        if (updatedProduct.getSubCategoryId() != null) {
            SubCategory subCategory = subCategoryRepo.findById(updatedProduct.getSubCategoryId().getSubCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sub Category not found"));
            existingProduct.setSubCategoryId(subCategory);
        }

        // Save updated product
        productRepo.save(existingProduct);
        return ResponseEntity.ok("Product updated successfully");
    }

    @GetMapping("/customer/{customerId}/order-count")
    public ResponseEntity<Integer> getCustomerOrderCount(
            @RequestHeader("Authorization") String token,
            @PathVariable int customerId) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body(null);
        }

        String token1 = token.substring(7);
        String username = jwtService.extractUsername(token1);
        User merchant = userRepo.findByUsername(username);
        if (merchant == null) {
            return ResponseEntity.status(404).body(null);
        }

        int merchantId = merchant.getUserId();
        int orderCount = orderRepo.countByUser_IdAndProduct_MerchantId(customerId, merchantId);
        return ResponseEntity.ok(orderCount);
    }
}