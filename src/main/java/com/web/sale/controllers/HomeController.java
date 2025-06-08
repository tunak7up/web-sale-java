package com.web.sale.controllers;

import com.web.sale.dto.CategoryDTO;
import com.web.sale.dto.ProductCardDTO;
import com.web.sale.models.Category;
import com.web.sale.models.Feedback;
import com.web.sale.models.Inventory;
import com.web.sale.models.Product;
import com.web.sale.services.CategoryRepository;
import com.web.sale.services.FeedbackRepository;
import com.web.sale.services.InventoryRepository;
import com.web.sale.services.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/")
    public String home(Model model) {
        // 1. Lấy và chuẩn bị dữ liệu cho Featured Categories
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();

        for (Category category : categories) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            // Lấy thumbnail từ Category model. Nếu null hoặc rỗng, dùng ảnh placeholder
            if (category.getThumbnail() != null && !category.getThumbnail().isEmpty()) {
                dto.setThumbnail(category.getThumbnail());
            } else {
                dto.setThumbnail("https://via.placeholder.com/300x200?text=Category+Image"); // Ảnh placeholder
            }
            categoryDTOs.add(dto);
        }
        model.addAttribute("categories", categoryDTOs);


        // 2. Lấy và chuẩn bị dữ liệu cho Featured Products (Sản phẩm mới nhất)
        // Lấy 4 sản phẩm mới nhất, không bị xóa
        List<Product> latestProducts = productRepository.findByDeleted(0, PageRequest.of(0, 4, Sort.by("createdAt").descending()));

        List<ProductCardDTO> productCardDTOs = new ArrayList<>();
        for (Product product : latestProducts) {
            ProductCardDTO dto = new ProductCardDTO();
            dto.setId(product.getId());
            dto.setTitle(product.getTitle());
            dto.setThumbnail(product.getThumbnail());
            dto.setOldPrice(product.getPrice());
            dto.setDiscount(product.getDiscount() != null ? product.getDiscount() : 0);

            if (product.getPrice() != null) {
                double discountedPrice = product.getPrice() * (1 - (dto.getDiscount() / 100.0));
                dto.setCurrentPrice((int) Math.round(discountedPrice));
            } else {
                dto.setCurrentPrice(0);
            }

            // Tính toán rating và reviewCount
            List<Feedback> feedbacks = feedbackRepository.findByProduct(product);
            dto.setReviewCount(feedbacks.size());
            double averageRating = feedbacks.stream()
                    .mapToInt(Feedback::getRating)
                    .filter(Objects::nonNull)
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(Math.round(averageRating * 10.0) / 10.0);

            // Tồn kho
            List<Inventory> inventories = inventoryRepository.findByProduct(product);
            Integer totalQuantity = inventories.stream()
                    .map(Inventory::getQuantity)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();
            dto.setInStock(totalQuantity > 0);
            dto.setInventoryStatus(totalQuantity > 0 ? (totalQuantity < 5 ? "Sắp hết hàng" : "Còn hàng") : "Hết hàng");

            productCardDTOs.add(dto);
        }
        model.addAttribute("latestProducts", productCardDTOs);

        return "index";
    }
}