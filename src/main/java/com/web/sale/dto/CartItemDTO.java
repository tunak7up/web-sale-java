package com.web.sale.dto;

// Đặt trong cùng package với ProductCardDTO
public class CartItemDTO {
    private int productId;
    private String productName;
    private String productThumbnail;
    private Double price; // Giá hiện tại của sản phẩm
    private int quantity;
    private Double subtotal; // price * quantity

    // Constructors, Getters, Setters

    public CartItemDTO() {
    }

    public CartItemDTO(int productId, String productName, String productThumbnail, Double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productThumbnail = productThumbnail;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = price * quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.price * this.quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}