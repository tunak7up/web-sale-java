package com.web.sale.controllers;

import com.web.sale.dto.InventoryAdjustmentDTO;
import com.web.sale.dto.ProductInventorySummaryDTO;
import com.web.sale.models.Inventory;
import com.web.sale.models.Product;
import com.web.sale.services.InventoryRepository;
import com.web.sale.services.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StoreRepository storeRepository; // Để đếm tổng số cửa hàng

    @GetMapping
    public String showInventory(Model model) {
        // Lấy tất cả inventory
        List<Inventory> allInventories = inventoryRepository.findAll();

        // Nhóm các inventory theo product_id
        Map<Product, List<Inventory>> inventoriesByProduct = allInventories.stream()
                .collect(Collectors.groupingBy(Inventory::getProduct));

        // Tạo danh sách DTO
        List<ProductInventorySummaryDTO> productSummaries = inventoriesByProduct.entrySet().stream()
                .map(entry -> new ProductInventorySummaryDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        model.addAttribute("productSummaries", productSummaries); // Gửi danh sách DTO tới view

        // Đếm tổng số cửa hàng (như bạn đã hỏi trước đó)
        long totalStores = storeRepository.count();
        model.addAttribute("totalStores", totalStores);

        return "inventory/inventory";
    }

    // Phương thức mới để xử lý yêu cầu điều chỉnh số lượng tồn kho
    @PostMapping("/adjust") // URL đầy đủ: /inventory/adjust
    @ResponseBody // Đảm bảo Spring biết đây là endpoint API trả về dữ liệu trực tiếp, không phải view
    public ResponseEntity<?> adjustInventory(@RequestBody InventoryAdjustmentDTO adjustmentDTO) {
        // Kiểm tra validation cơ bản cho số lượng
        if (adjustmentDTO.getQuantity() == null || adjustmentDTO.getQuantity() < 0) {
            return ResponseEntity.badRequest().body("Số lượng không hợp lệ. Vui lòng nhập số lượng không âm.");
        }

        try {
            // Tìm bản ghi Inventory cụ thể bằng product ID và store ID
            Optional<Inventory> optionalInventory = inventoryRepository.findByProductIdAndStoreId(
                    adjustmentDTO.getProductId(),
                    adjustmentDTO.getStoreId()
            );

            if (optionalInventory.isPresent()) {
                Inventory inventoryToUpdate = optionalInventory.get();
                inventoryToUpdate.setQuantity(adjustmentDTO.getQuantity());
                inventoryRepository.save(inventoryToUpdate); // Lưu thay đổi vào cơ sở dữ liệu

                // Trả về bản ghi Inventory đã cập nhật (hoặc một thông báo thành công)
                return ResponseEntity.ok(inventoryToUpdate);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy kho hàng cho sản phẩm ID " + adjustmentDTO.getProductId() + " và cửa hàng ID " + adjustmentDTO.getStoreId() + ".");
            }
        } catch (Exception e) {
            // Ghi log lỗi để gỡ lỗi
            System.err.println("Lỗi khi điều chỉnh số lượng kho: " + e.getMessage());
            e.printStackTrace(); // In toàn bộ stack trace để dễ debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi nội bộ khi cập nhật số lượng. Vui lòng thử lại sau.");
        }
    }

}
