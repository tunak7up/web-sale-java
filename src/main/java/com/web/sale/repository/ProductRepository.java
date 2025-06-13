package com.web.sale.repository;

import com.web.sale.models.Category;
import com.web.sale.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryAndDeleted(Category category, int deleted);

    List<Product> findByDeleted(int i, PageRequest createdAt);

    Page<Product> findByDeleted(int deleted, Pageable pageable);
}
