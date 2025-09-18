package com.example.Ecommerce.controller;

import com.example.Ecommerce.model.Category;
import com.example.Ecommerce.model.SubCategory;
import com.example.Ecommerce.repo.SubCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/subcategories")
public class SubCategoryController {

    @Autowired
    SubCategoryRepo subCategoryRepo;

    @GetMapping("/{categoryId}")
    public List<SubCategory> getAllSubCategories(@PathVariable Integer categoryId) {
        Category category = new Category();
        category.setCategoryId(categoryId);
        List<SubCategory> subCategories = subCategoryRepo.findByCategoryId(category);
        for (SubCategory subCategory : subCategories) {
            subCategory.setSubImage("http://localhost:8080/api/images/subcategories/" + subCategory.getSubImage());
        }
        return subCategories;
    }
}


//    @PostMapping("/subCategory")
//    public void addSubCategory(@RequestBody SubCategory subCategory){
//        subCategoryService.addSubCategory(subCategory);
//    }

