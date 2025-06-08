package com.web.sale.controllers;

import com.web.sale.dto.ProductCardDTO;
import com.web.sale.models.Category;
import com.web.sale.models.Inventory;
import com.web.sale.models.Product;
import com.web.sale.services.CategoryRepository;
import com.web.sale.services.InventoryRepository;
import com.web.sale.services.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects; // Import Objects for null-safe stream operations

@Controller
@RequestMapping("/categories") // Map các URL bắt đầu bằng /categories
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/{categoryId}") // Ví dụ: /categories/1 (cho danh mục có ID là 1)
    public String showCategoryPage(@PathVariable("categoryId") int categoryId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Danh mục không tìm thấy.");
            return "redirect:/"; // Chuyển hướng về trang chủ hoặc trang lỗi nếu danh mục không tồn tại
        }
        Category category = optionalCategory.get();
        model.addAttribute("category", category); // Thêm đối tượng danh mục vào model

        // Lấy danh sách sản phẩm thuộc danh mục này và chưa bị xóa (deleted = 0)
        List<Product> products = productRepository.findByCategoryAndDeleted(category, 0);

        List<ProductCardDTO> productCards = new ArrayList<>();
        for (Product product : products) {
            ProductCardDTO dto = new ProductCardDTO();
            dto.setId(product.getId());
            dto.setTitle(product.getTitle());
            dto.setCategoryName(category.getName()); // Tên danh mục của sản phẩm
            dto.setThumbnail(product.getThumbnail());
            dto.setDiscount(product.getDiscount() != null ? product.getDiscount() : 0);
            dto.setOldPrice(product.getPrice());

            // Tính toán giá hiện tại sau giảm giá
            if (product.getPrice() != null) {
                double discountedPrice = product.getPrice() * (1 - (dto.getDiscount() / 100.0));
                dto.setCurrentPrice((int) Math.round(discountedPrice));
            } else {
                dto.setCurrentPrice(0); // Hoặc xử lý null tùy theo logic nghiệp vụ
            }

            // Xác định trạng thái tồn kho và cờ "inStock"
            List<Inventory> inventories = inventoryRepository.findByProduct(product);
            Integer totalQuantity = inventories.stream()
                    .map(Inventory::getQuantity)
                    .filter(Objects::nonNull) // Lọc bỏ các giá trị null
                    .mapToInt(Integer::intValue)
                    .sum();

            dto.setInStock(totalQuantity > 0);
            if (totalQuantity > 0) {
                dto.setInventoryStatus("Còn hàng");
                // Ví dụ: Đặt "Sắp hết hàng" nếu số lượng dưới ngưỡng nào đó
                if (totalQuantity < 5) { // Ngưỡng ví dụ
                    dto.setInventoryStatus("Sắp hết hàng");
                }
            } else {
                dto.setInventoryStatus("Hết hàng");
            }

            // --- Điền dữ liệu giả định cho các thông số kỹ thuật và đánh giá ---
            dto.setCpu("Intel Core i7"); // Thay thế bằng dữ liệu thực tế
            dto.setRam("16GB");         // Thay thế bằng dữ liệu thực tế
            dto.setStorage("512GB SSD"); // Thay thế bằng dữ liệu thực tế
            dto.setAverageRating(4.5);  // Thay thế bằng dữ liệu thực tế
            dto.setReviewCount(25);     // Thay thế bằng dữ liệu thực tế
            // --- Kết thúc dữ liệu giả định ---

            productCards.add(dto);
        }

        model.addAttribute("productCards", productCards); // Thêm danh sách DTO sản phẩm vào model
        model.addAttribute("productCount", productCards.size()); // Tổng số sản phẩm trong danh mục

        return "category"; // Trả về tên file HTML: category.html (trong src/main/resources/templates/)
    }
}