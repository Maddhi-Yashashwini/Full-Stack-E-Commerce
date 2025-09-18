package com.example.Ecommerce.repo;

import com.example.Ecommerce.model.Category;
import com.example.Ecommerce.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategory, Integer> {
    Optional<SubCategory> findBySubCategoryName(String subCategoryName);
    List<SubCategory> findByCategoryId(Category categoryId);
}
