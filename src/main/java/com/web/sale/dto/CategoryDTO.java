package com.web.sale.dto;

public class CategoryDTO {
    private int id;
    private String name;
    private String thumbnail; // Thêm trường này để hiển thị ảnh danh mục trên trang chủ

    // Constructors, Getters, Setters

    public CategoryDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}