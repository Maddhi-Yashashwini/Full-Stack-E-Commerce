package com.example.Ecommerce.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    // Define base directory (inside the target folder)
    private final Path CATEGORY_IMAGE_DIRECTORY = Paths.get("C:/Users/Maddhi.Yashashwini/springBoot projects/Ecommerce/src/main/resources/EcomImages/EcomCategoriesImages/");
    private final Path SUBCATEGORY_IMAGE_DIRECTORY = Paths.get("C:/Users/Maddhi.Yashashwini/springBoot projects/Ecommerce/src/main/resources/EcomImages/EcomSubCategoriesImages/");
    private final Path PRODUCT_IMAGE_DIRECTORY = Paths.get("C:/Users/Maddhi.Yashashwini/springBoot projects/Ecommerce/src/main/resources/EcomImages/EcomProductsImages/");
    private final Path USER_IMAGE_DIRECTORY = Paths.get("C:/Users/Maddhi.Yashashwini/springBoot projects/Ecommerce/src/main/resources/EcomImages/");

    @GetMapping("/categories/{imageName}")
    public ResponseEntity<Resource> getCategoryImage(@PathVariable String imageName) {
        return serveImage(CATEGORY_IMAGE_DIRECTORY, imageName);
    }

    @GetMapping("/subcategories/{imageName}")
    public ResponseEntity<Resource> getSubCategoryImage(@PathVariable String imageName) {
        return serveImage(SUBCATEGORY_IMAGE_DIRECTORY, imageName);
    }

    @GetMapping("/products/{imageName}")
    public ResponseEntity<Resource> getProductImage(@PathVariable String imageName) {
        return serveImage(PRODUCT_IMAGE_DIRECTORY, imageName);
    }

    @GetMapping("/users/{imageName}")
    public ResponseEntity<Resource> getUserImage(@PathVariable String imageName) {
        return serveImage(USER_IMAGE_DIRECTORY, imageName);
    }

    private ResponseEntity<Resource> serveImage(Path directory, String imageName) {
        try {
            Path imagePath = directory.resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(imagePath);
                if (contentType == null) {
                    contentType = "image/avif";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

