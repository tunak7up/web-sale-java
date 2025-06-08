// src/main/java/com/web/sale/models/Product.java
package com.web.sale.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime; // Sử dụng LocalDateTime cho ngày tháng

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "category_id") // Đảm bảo khớp với cột category_id trong DB
    private Category category;

    @Column(length = 255) // title varchar(255)
    private String title;

    private Integer price; // price int

    private Integer discount; // discount int

    @Lob // Dùng @Lob cho các trường kiểu LONGTEXT/TEXT
    @Column(name = "thumbnail") // thumbnail longtext
    private String thumbnail; // Lưu URL ảnh hoặc base64 string

    @Lob // Dùng @Lob cho các trường kiểu LONGTEXT/TEXT
    @Column(name = "description") // description longtext
    private String description;

    private Integer deleted; // deleted int (ví dụ: 0 = không xóa, 1 = đã xóa mềm)

    @CreationTimestamp // Tự động set thời gian khi tạo mới
    @Column(name = "created_at", updatable = false) // created_at datetime
    private LocalDateTime createdAt;

    @UpdateTimestamp // Tự động cập nhật thời gian khi thay đổi
    @Column(name = "updated_at") // updated_at datetime
    private LocalDateTime updatedAt;

    // Constructors
    public Product() {
    }

    // Constructor bỏ qua id, createdAt, updatedAt vì chúng được tự động tạo/cập nhật
    public Product(Category category, String title, Integer price, Integer discount, String thumbnail, String description, Integer deleted) {
        this.category = category;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.thumbnail = thumbnail;
        this.description = description;
        this.deleted = deleted;
    }

    // Getters and Setters (đảm bảo đầy đủ cho tất cả các trường)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}