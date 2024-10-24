package com.example.EcommerceBackend.DTO;

import com.example.EcommerceBackend.Entity.Product;
import com.example.EcommerceBackend.Entity.User;

import java.util.List;

public class WishlistDto {

    private Integer id;
    private List<Product> product;

    private Integer userId;

    public WishlistDto() {

    }

    public WishlistDto(Integer id, List<ProductDto> productDtos, Integer userId) {
        this.id = id;
        this.product = product;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
