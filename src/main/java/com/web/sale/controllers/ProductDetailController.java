package com.web.sale.controllers;

import com.web.sale.dto.ProductCardDTO;
import com.web.sale.dto.ProductDetailDTO;
import com.web.sale.models.Product;
import com.web.sale.models.Inventory;
import com.web.sale.models.Feedback; // Đã đổi từ Review sang Feedback
import com.web.sale.repository.ProductRepository;
import com.web.sale.repository.InventoryRepository;
import com.web.sale.repository.FeedbackRepository; // Đã đổi từ ReviewRepository sang FeedbackRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProductDetailController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private FeedbackRepository feedbackRepository; // Đã cập nhật để sử dụng FeedbackRepository

    @GetMapping("/products/{id}")
    public String showProductDetail(@PathVariable int id, Model model) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty() || productOptional.get().getDeleted() == 1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại hoặc đã bị xóa");
        }

        Product product = productOptional.get();

        // 1. Chuẩn bị ProductDetailDTO
        ProductDetailDTO productDetail = new ProductDetailDTO();
        productDetail.setId(product.getId());
        productDetail.setTitle(product.getTitle());
        productDetail.setThumbnail(product.getThumbnail());
        productDetail.setOldPrice(product.getPrice());
        productDetail.setDiscount(product.getDiscount() != null ? product.getDiscount() : 0);

        // Tính toán giá hiện tại
        if (product.getPrice() != null) {
            double discountedPrice = product.getPrice() * (1 - (productDetail.getDiscount() / 100.0));
            productDetail.setCurrentPrice((int) Math.round(discountedPrice));
        } else {
            productDetail.setCurrentPrice(0);
        }

        // Sử dụng trường description duy nhất
        productDetail.setDescription(product.getDescription());

        // Lấy thông tin tồn kho
        List<Inventory> inventories = inventoryRepository.findByProduct(product);
        Integer totalQuantity = inventories.stream()
                .map(Inventory::getQuantity)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
        productDetail.setTotalStock(totalQuantity);
        productDetail.setInStock(totalQuantity > 0);
        productDetail.setInventoryStatus(totalQuantity > 0 ? (totalQuantity < 5 ? "Sắp hết hàng" : "Còn hàng") : "Hết hàng");

        // Lấy thông tin feedback/reviews
        List<Feedback> feedbacks = feedbackRepository.findByProduct(product); // Đã đổi từ reviews sang feedbacks
        productDetail.setReviewCount(feedbacks.size());
        double averageRating = feedbacks.stream()
                .mapToInt(Feedback::getRating) // Giả định Feedback có getRating() trả về Integer
                .filter(Objects::nonNull) // Lọc các rating null
                .average()
                .orElse(0.0);
        productDetail.setAverageRating(Math.round(averageRating * 10.0) / 10.0); // Làm tròn 1 chữ số thập phân

        // Bạn có thể thêm ProductImage DTO hoặc danh sách URL hình ảnh phụ
        // Hiện tại vẫn hardcode ảnh phụ như trước, bạn có thể thay đổi bằng cách lưu trong DB
        productDetail.setProductImages(List.of(
                "https://dlcdnwebimgs.asus.com/files/media/8B74E7EE-B66A-4420-894E-3C3B980312EE/v2/img/design/color/strix-g-2022-pink.png",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRbxqRcTQYIWuEbTJFGDL8pEZJombb9sV5h6Q&s",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMd9x3t8Q62J_oF3K3eqjbwm9NkiTyaQV9mw&s",
                "https://i0.wp.com/walastech.com/wp-content/uploads/2022/06/asus-rog-strix-g15-2022-philippines-review-7.jpg?resize=1024%2C768&ssl=1"
        ));


        // Lấy sản phẩm tương tự (ví dụ: cùng danh mục, không phải chính nó, sắp xếp theo ID giảm dần)
        List<Product> relatedProductsEntities = productRepository.findByCategoryAndDeleted(product.getCategory(), 0)
                .stream()
                .filter(p -> p.getId() != product.getId()) // Loại bỏ chính sản phẩm đang xem
                .limit(3) // Lấy tối đa 3 sản phẩm tương tự
                .collect(Collectors.toList());

        List<ProductCardDTO> relatedProductCards = new ArrayList<>();
        for (Product relatedProduct : relatedProductsEntities) {
            ProductCardDTO dto = new ProductCardDTO();
            dto.setId(relatedProduct.getId());
            dto.setTitle(relatedProduct.getTitle());
            dto.setThumbnail(relatedProduct.getThumbnail());
            dto.setDiscount(relatedProduct.getDiscount() != null ? relatedProduct.getDiscount() : 0);
            dto.setOldPrice(relatedProduct.getPrice());

            if (relatedProduct.getPrice() != null) {
                double discountedPrice = relatedProduct.getPrice() * (1 - (dto.getDiscount() / 100.0));
                dto.setCurrentPrice((int) Math.round(discountedPrice));
            } else {
                dto.setCurrentPrice(0);
            }

            // Tồn kho cho sản phẩm tương tự
            List<Inventory> relatedInventories = inventoryRepository.findByProduct(relatedProduct);
            Integer relatedTotalQuantity = relatedInventories.stream()
                    .map(Inventory::getQuantity)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();
            dto.setInStock(relatedTotalQuantity > 0);
            dto.setInventoryStatus(relatedTotalQuantity > 0 ? (relatedTotalQuantity < 5 ? "Sắp hết hàng" : "Còn hàng") : "Hết hàng");

            // Các thuộc tính giả định cho ProductCardDTO nếu không có trong Product model
            dto.setCpu("N/A"); // Hoặc lấy từ Product nếu có trường này (hiện tại không có)
            dto.setRam("N/A"); // Hoặc lấy từ Product nếu có trường này (hiện tại không có)

            // Tính toán rating và reviewCount cho sản phẩm tương tự nếu có
            List<Feedback> relatedFeedbacks = feedbackRepository.findByProduct(relatedProduct);
            dto.setReviewCount(relatedFeedbacks.size());
            double relatedAverageRating = relatedFeedbacks.stream()
                    .mapToInt(Feedback::getRating)
                    .filter(Objects::nonNull)
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(Math.round(relatedAverageRating * 10.0) / 10.0);

            relatedProductCards.add(dto);
        }

        model.addAttribute("product", productDetail);
        model.addAttribute("relatedProducts", relatedProductCards);
        model.addAttribute("category", product.getCategory()); // Để dùng cho breadcrumb

        return "product-detail"; // Trả về tên file HTML template
    }
}