package com.example.Ecommerce.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ImageService {


    private final String PRODUCT_IMAGE_UPLOAD_PATH = "C:/Users/Maddhi.Yashashwini/springBoot projects/Ecommerce/Backend/src/main/resources/EcomImages/EcomProductsImages/";
    private final String USER_IMAGE_UPLOAD_PATH = "C:/Users/Maddhi.Yashashwini/springBoot projects/Ecommerce/Backend/src/main/resources/EcomImages/";


    public List<String> uploadImages(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Please select images to upload.");
        }


        List<String> filenames = new ArrayList<>();
        File uploadDir = new File(PRODUCT_IMAGE_UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }


        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                File destinationFile = new File(PRODUCT_IMAGE_UPLOAD_PATH + file.getOriginalFilename());
                file.transferTo(destinationFile);


                if (destinationFile.exists()) {
                    filenames.add(file.getOriginalFilename());
                } else {
                    throw new IOException("File upload failed for: " + file.getOriginalFilename());
                }
            }
        }
        return filenames;
    }
    public String uploadUserProfileImage(MultipartFile file, int userId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select an image to upload.");
        }

        File uploadDir = new File(USER_IMAGE_UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filename = file.getOriginalFilename();
        File destinationFile = new File(USER_IMAGE_UPLOAD_PATH + filename);
        file.transferTo(destinationFile);

        if (destinationFile.exists()) {
            return filename;
        } else {
            throw new IOException("File upload failed for: " + file.getOriginalFilename());
        }
    }
}