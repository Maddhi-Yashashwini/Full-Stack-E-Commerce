package com.example.Ecommerce.model;
import jakarta.persistence.*;

@Entity

public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subCategoryId;

    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category categoryId;

    private String subCategoryName;
    private String subImage;


    public SubCategory() {  }

    public SubCategory(int subCategoryId, Category categoryId, String subCategoryName, String subImage) {
        this.subCategoryId = subCategoryId;
        this.categoryId = categoryId;
        this.subCategoryName = subCategoryName;
        this.subImage = subImage;
    }

    public int getSubCategoryId() {  return subCategoryId;  }
    public void setSubCategoryId(int subCategoryId) {  this.subCategoryId = subCategoryId;  }

    public Category getCategoryId() {  return categoryId;  }
    public void setCategoryId(Category category) {  this.categoryId = category;  }

    public String getSubCategoryName() {  return subCategoryName;  }
    public void setSubCategoryName(String subCategoryName) {  this.subCategoryName = subCategoryName;  }

    public String getSubImage() {  return subImage;  }
    public void setSubImage(String subImage) {  this.subImage = subImage;  }
}
