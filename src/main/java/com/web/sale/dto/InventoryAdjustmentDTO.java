// src/main/java/com/web/sale/dto/InventoryAdjustmentDTO.java
package com.web.sale.dto;

public class InventoryAdjustmentDTO {
    private int productId;
    private int storeId;
    private Integer quantity;

    public InventoryAdjustmentDTO() {}

    public InventoryAdjustmentDTO(int productId, int storeId, Integer quantity) {
        this.productId = productId;
        this.storeId = storeId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}