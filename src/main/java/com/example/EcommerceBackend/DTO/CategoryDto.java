package com.example.EcommerceBackend.DTO;

import java.util.List;

public class CategoryDto {
    private int id;
    private String categoryName;
    private String description;
    private List<ProductDto> products;

    public CategoryDto() {
    }

    public CategoryDto(int id, String categoryName, String description, List<ProductDto> products) {
        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.products = products;
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

}