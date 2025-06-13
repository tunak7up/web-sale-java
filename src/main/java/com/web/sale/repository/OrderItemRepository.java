package com.web.sale.repository;

import com.web.sale.models.OrderItem;
import com.web.sale.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    // Tìm các mục đơn hàng thuộc một đơn hàng cụ thể
    List<OrderItem> findByOrder(Order order);
}