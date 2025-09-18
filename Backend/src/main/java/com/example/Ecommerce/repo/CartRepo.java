package com.example.Ecommerce.repo;

import com.example.Ecommerce.model.Cart;
import com.example.Ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    Cart findByUserId(int userId);
}
