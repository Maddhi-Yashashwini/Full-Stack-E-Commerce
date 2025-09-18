package com.example.Ecommerce.controller;

import com.example.Ecommerce.model.Product;
import com.example.Ecommerce.repo.ProductRepo;
import com.example.Ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000") // Adjust if needed
public class ProductController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        List<Product> products =  productRepo.findAll();
        for(Product product:products){
            product.setImage1("http://localhost:8080/api/images/products/" + product.getImage1());
        }
        return products;
    }

    // Get products by category and subcategory
    @GetMapping("/{categoryId}/{subCategoryId}")
    public List<Product> getProductsByCategoryAndSubCategory(
            @PathVariable Integer categoryId,
            @PathVariable Integer subCategoryId) {
        List<Product> products = productRepo.findByCategoryId_IdAndSubCategoryId_SubCategoryId(categoryId, subCategoryId);
        for(Product product:products){
            product.setImage1("http://localhost:8080/api/images/products/" + product.getImage1());
        }
        return products;
    }

    @GetMapping("/")
    public void get(){
        productService.greet();
    }

    // Get a specific product by ID
    @GetMapping("/{categoryId}/{subCategoryId}/{productId}")
    public Product getProductById(@PathVariable Integer productId) {
        Product product = productRepo.findByProductId(productId);
        product.setImage1("http://localhost:8080/api/images/products/" + product.getImage1());
        product.setImage2("http://localhost:8080/api/images/products/" + product.getImage2());
        product.setImage3("http://localhost:8080/api/images/products/" + product.getImage3());
        product.setImage4("http://localhost:8080/api/images/products/" + product.getImage4());
        return product;
    }
}


