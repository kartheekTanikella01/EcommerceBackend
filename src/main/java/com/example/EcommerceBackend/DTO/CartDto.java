package com.example.EcommerceBackend.DTO;

import java.util.List;

public class CartDto {
    private int id;
    private double totalPrice;
    private List<CartItemDto> items;

    public CartDto() {
    }

    public CartDto(int id, double totalPrice, List<CartItemDto> items) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }
}
