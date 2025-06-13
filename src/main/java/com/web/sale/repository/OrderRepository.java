package com.web.sale.repository;

import com.web.sale.models.Order;
import com.web.sale.models.User; // Cần import User model nếu bạn có
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Tìm các đơn hàng theo người dùng (nếu có user_id)
    List<Order> findByUser(User user);
    // Tìm các đơn hàng theo trạng thái
    List<Order> findByStatus(String status);
    Page<Order> findByStatus(String status, Pageable pageable);

}