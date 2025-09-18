package com.example.Ecommerce.repo;

import com.example.Ecommerce.model.order.Order;
import com.example.Ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
    List<Order> findByUserOrderByCreatedAtDesc(@Param("user") User user);
    List<Order> findByProduct_MerchantId(Long merchantId);
    Optional<Order> findById(Integer id);
    int countByUser_IdAndProduct_MerchantId(int userId, int merchantId);
}