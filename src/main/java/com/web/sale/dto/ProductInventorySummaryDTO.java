// Đặt trong một package thích hợp, ví dụ: com.web.sale.dto

package com.web.sale.dto;

import com.web.sale.models.Product;
import com.web.sale.models.Inventory;

import java.util.List;

public class ProductInventorySummaryDTO {
    private Product product;
    private List<Inventory> inventories; // Danh sách các Inventory của sản phẩm này
    private Integer totalQuantityAcrossStores; // Tổng số lượng của sản phẩm này trên tất cả các kho

    // Constructor
    public ProductInventorySummaryDTO(Product product, List<Inventory> inventories) {
        this.product = product;
        this.inventories = inventories;
        // Tính tổng số lượng
        this.totalQuantityAcrossStores = inventories.stream()
                .mapToInt(Inventory::getQuantity)
                .sum();
    }

    // Getters
    public Product getProduct() {
        return product;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public Integer getTotalQuantityAcrossStores() {
        return totalQuantityAcrossStores;
    }

    // Setters (tùy chọn, nếu bạn cần thay đổi dữ liệu sau khi khởi tạo)
    public void setProduct(Product product) {
        this.product = product;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
        this.totalQuantityAcrossStores = inventories.stream()
                .mapToInt(Inventory::getQuantity)
                .sum();
    }
    public void setTotalQuantityAcrossStores(Integer totalQuantityAcrossStores) {
        this.totalQuantityAcrossStores = totalQuantityAcrossStores;
    }
}