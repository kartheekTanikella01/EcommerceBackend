package com.example.EcommerceBackend.DTO;

public class ReviewDTO {
    private Integer id;
    private Integer productId;
    private Integer userId;
    private Integer stars;

    private String comment;

    public ReviewDTO() {
    }

    public ReviewDTO(Integer id, Integer productId, Integer userId, Integer stars,String comment) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.stars = stars;
        this.comment=comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }
}
