// src/main/java/com/web/sale/dto/InitialInventoryItemDTO.java
package com.web.sale.dto;

public class InitialInventoryItemDTO {
    private int storeId;
    private Integer quantity;

    // Constructors
    public InitialInventoryItemDTO() {}

    public InitialInventoryItemDTO(int storeId, Integer quantity) {
        this.storeId = storeId;
        this.quantity = quantity;
    }

    // Getters and Setters
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