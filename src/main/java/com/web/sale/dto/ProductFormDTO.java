// src/main/java/com/web/sale/dto/ProductFormDTO.java
package com.web.sale.dto;

import java.util.List;

public class ProductFormDTO {
    private Integer id;
    private String title;
    private Integer categoryId;
    private Integer price;
    private Integer discount;
    private String thumbnail;
    private String description;
    private List<InitialInventoryItemDTO> initialInventories; // Để lưu số lượng ban đầu ở các kho

    // Getters and Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public List<InitialInventoryItemDTO> getInitialInventories() {
        return initialInventories;
    }

    public void setInitialInventories(List<InitialInventoryItemDTO> initialInventories) {
        this.initialInventories = initialInventories;
    }
}