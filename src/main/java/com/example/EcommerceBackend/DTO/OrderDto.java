package com.example.EcommerceBackend.DTO;

import java.util.Date;
import java.util.List;

public class OrderDto {
    private int id;
    private double totalPrice;
    private Date orderDate;
    private List<OrderItemDto> items;

    public OrderDto() {
    }

    public OrderDto(int id, double totalPrice, Date orderDate, List<OrderItemDto> items) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.items = items;
    }
    // Getters and setters


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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}
