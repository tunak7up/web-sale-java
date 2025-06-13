package com.web.sale.repository;

import com.web.sale.models.Feedback;
import com.web.sale.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    // Phương thức tùy chỉnh để tìm tất cả feedback của một sản phẩm
    List<Feedback> findByProduct(Product product);
}