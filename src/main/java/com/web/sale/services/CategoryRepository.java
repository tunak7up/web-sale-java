// src/main/java/com/web/sale/services/CategoryRepository.java
package com.web.sale.services;

import com.web.sale.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}