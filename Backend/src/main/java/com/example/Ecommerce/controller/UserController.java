//package com.example.Ecommerce.controller;
//
//import com.example.Ecommerce.config.LoginRequest;
//import com.example.Ecommerce.model.Cart;
//import com.example.Ecommerce.model.User;
//import com.example.Ecommerce.repo.CartRepo;
//import com.example.Ecommerce.repo.UserRepo;
//import com.example.Ecommerce.service.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//        import java.util.HashMap;
//import java.util.Map;
//
//@CrossOrigin(origins = "http://127.0.0.1:5500")
//@RestController
//@RequestMapping("/api")
//public class UserController {
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Autowired
//    private CartRepo cartRepo;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtService jwtService;
//
//    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
//        if (userRepo.findByUsername(user.getUsername()) != null) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User already exists!"));
//        }
//
//        // Encode password before saving user
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole("CUSTOMER"); // Default role
//        User savedUser = userRepo.save(user);
//
//        // Create and assign a cart to the registered user
//        Cart cart = new Cart();
//        cart.setUser(savedUser);
//        cart.setTotalPrice(0.0);
//        cartRepo.save(cart);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully! Cart created."));
//    }
//
//
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginRequest user) {
//        User existingUser = userRepo.findByUsername(user.username());
//
//        if (existingUser == null || !passwordEncoder.matches(user.password(), existingUser.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials!"));
//        }
//
//        String token = jwtService.generateAccessToken(existingUser);
//        Map<String, Object> response = new HashMap<>();
//        response.put("token", token);
//        response.put("username", existingUser.getUsername());
//        response.put("role", existingUser.getRole());
//
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/user/profile")
//    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) {
//        String username = jwtService.extractUsername(token.substring(7));
//        User user = userRepo.findByUsername(username);
//        return ResponseEntity.ok(user);
//    }
//
//    @PutMapping("/user/profile/update")
//    public ResponseEntity<String> updateUserProfile(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
//        String username = jwtService.extractUsername(token.substring(7));
//        User user = userRepo.findByUsername(username);
//
//        user.setEmail(updatedUser.getEmail());
//        user.setPhone(updatedUser.getPhone());
//        user.setAddress(updatedUser.getAddress());
//
//        userRepo.save(user);
//        return ResponseEntity.ok("Profile updated successfully!");
//    }
//
//}
//


package com.example.Ecommerce.controller;

import com.example.Ecommerce.config.LoginRequest;
import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.User;
import com.example.Ecommerce.repo.CartRepo;
import com.example.Ecommerce.repo.UserRepo;
import com.example.Ecommerce.service.ImageService;
import com.example.Ecommerce.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User already exists!"));
        }

        // Encode password before saving user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("CUSTOMER"); // Default role
        User savedUser = userRepo.save(user);

        // Create and assign a cart to the registered user
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cart.setTotalPrice(0.0);
        cartRepo.save(cart);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User registered successfully! Cart created."));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginRequest user) {
        User existingUser = userRepo.findByUsername(user.username());

        if (existingUser == null || !passwordEncoder.matches(user.password(), existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials!"));
        }

        String token = jwtService.generateAccessToken(existingUser);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", existingUser.getUsername());
        response.put("role", existingUser.getRole());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.substring(7));
        User user = userRepo.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/profile/update")
    public ResponseEntity<String> updateUserProfile(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        String username = jwtService.extractUsername(token.substring(7));
        User user = userRepo.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());

        userRepo.save(user);
        return ResponseEntity.ok("Profile updated successfully!");
    }

    @PutMapping("/user/profile/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> passwordMap) {
        String username = jwtService.extractUsername(token.substring(7));
        User user = userRepo.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect old password.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        return ResponseEntity.ok("Password updated successfully!");
    }

    @PostMapping("/user/profile/upload-image")
    public ResponseEntity<String> uploadUserProfileImage(@RequestHeader("Authorization") String token, @RequestParam("image") MultipartFile image) {
        String username = jwtService.extractUsername(token.substring(7));
        User user = userRepo.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        try {
            String imageUrl = imageService.uploadUserProfileImage(image, user.getUserId());
            user.setUserImage(imageUrl);
            userRepo.save(user);
            return ResponseEntity.ok("Profile image uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile image: " + e.getMessage());
        }
    }
}

