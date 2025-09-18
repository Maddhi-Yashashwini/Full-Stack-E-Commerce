package com.example.Ecommerce.model;
import jakarta.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment the id
    private int id;
    private String name;
    private String image;


    public Category() { }

    public Category(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getCategoryId() {  return id;  }
    public void setCategoryId(int id) {  this.id = id;  }

    public String getName() {  return name;  }
    public void setName(String name) {  this.name = name;  }

    public String getImage() {  return image;  }
    public void setImage(String image) {  this.image = image;  }

}
