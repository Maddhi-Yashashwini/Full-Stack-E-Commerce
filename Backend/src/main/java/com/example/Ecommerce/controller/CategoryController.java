package com.example.Ecommerce.controller;

import com.example.Ecommerce.model.Category;
import com.example.Ecommerce.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:5500")
//@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    CategoryRepo categoryRepo;

    @GetMapping("/categories")
    public List<Category> getAllCategories(){

        List<Category> categories = categoryRepo.findAll();
        for (Category category : categories) {
            category.setImage("http://localhost:8080/api/images/categories/" + category.getImage());
        }
        return categories;
    }

}
