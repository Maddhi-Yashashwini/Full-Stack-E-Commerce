package com.example.Ecommerce.service;
import com.example.Ecommerce.model.Product;
import com.example.Ecommerce.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepo repo;

    public List<Product> getAllProducts(){
        return repo.findAll();
    }

    public void addProduct(Product product) {
        repo.save(product);
    }

    public String  greet(){
        return "Hello" ;
    }
}
