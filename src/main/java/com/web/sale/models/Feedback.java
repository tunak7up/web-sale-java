package com.web.sale.models;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // Liên kết với bảng product

    @Column(nullable = true)
    private Integer rating; // Đánh giá sao (ví dụ: 1 đến 5)

    @Column(columnDefinition = "TEXT") // Dùng TEXT cho description
    private String description; // Nội dung feedback/đánh giá

    // Constructors, Getters, Setters

    public Feedback() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}