package com.example.Ecommerce.model;
import jakarta.persistence.*;

@Entity

public class Product {

    @ManyToOne
    @JoinColumn(name = "subCategoryId", referencedColumnName = "subCategoryId")
    private SubCategory subCategoryId;

    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category categoryId;

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private int merchantId;
    private String name;
    private String description;
    private double price;
    private int discount;
    @Column(name = "discount_price")
    private int disPrice;
    private int stock;
    private String image1, image2, image3, image4;


    public Product() { }

    public Product(int productId, int merchantId, String name, String description, double price, int stock, String image1, String image2, String image3, String image4, Category categoryId, SubCategory subCategoryId, int discount, int disPrice) {
        this.merchantId = merchantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.disPrice = disPrice;
        this.stock = stock;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;

    }

    public void setProductId(int productId) { this.productId = productId; }
    public int getProductId() {  return productId; }

    public void setMerchantId(int merchantId) { this.merchantId = merchantId; }
    public int getMerchantId() {  return merchantId; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description; }

    public void setPrice(double price) { this.price = price; }
    public double getPrice() { return price; }

    public void setStock(int stock) { this.stock = stock; }
    public int getStock() { return stock; }

    public void setImage1(String image1) { this.image1 = image1; }
    public String getImage1() { return image1; }

    public void setImage2(String image2) { this.image2 = image2; }
    public String getImage2() { return image2; }

    public void setImage3(String image3) { this.image3 = image3; }
    public String getImage3() { return image3; }

    public void setImage4(String image4) { this.image4 = image4; }
    public String getImage4() { return image4; }

    public void setSubCategoryId(SubCategory subCategoryId) { this.subCategoryId = subCategoryId; }
    public SubCategory getSubCategoryId() { return subCategoryId; }

    public void setCategoryId(Category categoryId) { this.categoryId = categoryId; }
    public Category getCategoryId() { return categoryId; }

    public int getDiscount() { return discount; }
    public void setDiscount(int discount) { this.discount = discount; }

    public int getDisPrice() { return disPrice; }
    public void setDisPrice(int disPrice) { this.disPrice = disPrice; }
}

