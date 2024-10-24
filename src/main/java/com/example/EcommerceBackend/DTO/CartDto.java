package com.example.EcommerceBackend.DTO;



import com.example.EcommerceBackend.Entity.Product;

import java.util.ArrayList;
import java.util.List;

public class CartDto {

    private Integer userid;

    private List<ProductDto> product=new ArrayList<>();


    private double totalPrice; // New field

    public CartDto() {
    }

    public CartDto(Integer userid) {
        this.userid = userid;
    }

    public CartDto(List<ProductDto> product) {

        this.product = product;
    }

    public CartDto(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CartDto(int userId, List<Product> products) {
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public List<ProductDto> getProduct() {
        return product;
    }

    public void setProduct(List<ProductDto> product) {
        this.product = product;
    }
}
