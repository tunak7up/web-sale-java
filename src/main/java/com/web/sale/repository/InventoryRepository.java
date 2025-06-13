package com.web.sale.repository;

import com.web.sale.models.Inventory;
import com.web.sale.models.Product;
import com.web.sale.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByProduct(Product product); // Tìm tất cả tồn kho của một sản phẩm
    Optional<Inventory> findByProductAndStore(Product product, Store store); // Tìm tồn kho của một sản phẩm tại một cửa hàng cụ thể

    Optional<Inventory> findByProductIdAndStoreId(int productId, int storeId);
}