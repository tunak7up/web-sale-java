package com.web.sale.controllers;

import com.web.sale.dto.InitialInventoryItemDTO;
import com.web.sale.dto.ProductFormDTO;
import com.web.sale.models.Category;
import com.web.sale.models.Inventory;
import com.web.sale.models.Product;
import com.web.sale.models.Store;
import com.web.sale.services.CategoryRepository;
import com.web.sale.services.InventoryRepository;
import com.web.sale.services.ProductRepository;
import com.web.sale.services.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable; // Import PathVariable
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList; // Import ArrayList
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors; // Import Collectors

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    // Hiển thị form thêm sản phẩm
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productForm", new ProductFormDTO());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("stores", storeRepository.findAll());
        return "product/addproduct"; // Đảm bảo đúng đường dẫn nếu bạn đã di chuyển
    }

    // Xử lý khi submit form thêm sản phẩm
    @PostMapping("/add")
    public String addProduct(@ModelAttribute("productForm") ProductFormDTO productForm, RedirectAttributes redirectAttributes) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(productForm.getCategoryId());
            if (optionalCategory.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Danh mục không tồn tại.");
                return "redirect:/products/add";
            }
            Category category = optionalCategory.get();

            Product product = new Product();
            product.setTitle(productForm.getTitle());
            product.setCategory(category);
            product.setPrice(productForm.getPrice());
            product.setDiscount(productForm.getDiscount());
            product.setThumbnail(productForm.getThumbnail());
            product.setDescription(productForm.getDescription());
            product.setDeleted(0);

            Product savedProduct = productRepository.save(product);

            if (productForm.getInitialInventories() != null) {
                for (InitialInventoryItemDTO item : productForm.getInitialInventories()) {
                    Optional<Store> optionalStore = storeRepository.findById(item.getStoreId());
                    if (optionalStore.isPresent()) {
                        Store store = optionalStore.get();
                        Inventory inventory = new Inventory();
                        inventory.setProduct(savedProduct);
                        inventory.setStore(store);
                        inventory.setQuantity(item.getQuantity() != null ? item.getQuantity() : 0);

                        inventoryRepository.save(inventory);
                    } else {
                        System.err.println("Cửa hàng không tồn tại với ID: " + item.getStoreId());
                    }
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được thêm thành công!");
            return "redirect:/inventory"; // Chuyển hướng về trang quản lý kho
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi thêm sản phẩm: " + e.getMessage());
            return "redirect:/products/add";
        }
    }

    // --- Phương thức hiển thị form chỉnh sửa sản phẩm ---
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tìm thấy.");
            return "redirect:/inventory";
        }
        Product product = optionalProduct.get();

        ProductFormDTO productForm = new ProductFormDTO();
        productForm.setId(product.getId());
        productForm.setTitle(product.getTitle());
        productForm.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        productForm.setPrice(product.getPrice());
        productForm.setDiscount(product.getDiscount());
        productForm.setThumbnail(product.getThumbnail());
        productForm.setDescription(product.getDescription());

        // CHỈNH SỬA PHẦN NÀY: Chuyển đổi danh sách tồn kho thành Map<StoreId, Quantity>
        List<Inventory> currentInventories = inventoryRepository.findByProduct(product);
        Map<Integer, Integer> currentInventoryQuantities = currentInventories.stream()
                .collect(Collectors.toMap(
                        inv -> inv.getStore().getId(),  // Key: ID của cửa hàng
                        inv -> inv.getQuantity() != null ? inv.getQuantity() : 0 // Value: Số lượng tồn kho (hoặc 0 nếu null)
                ));
        model.addAttribute("currentInventoryQuantities", currentInventoryQuantities); // Thêm Map vào Model

        // Chúng ta vẫn cần productForm để điền các trường khác như title, categoryId, v.v.
        // Tuy nhiên, không cần set initialInventories vào productForm DTO nữa nếu chỉ dùng Map
        // productForm.setInitialInventories(initialInventories); // Có thể bỏ dòng này nếu không dùng đến

        model.addAttribute("productForm", productForm); // Vẫn cần productForm cho các trường khác
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("stores", storeRepository.findAll());
        return "product/editproduct";
    }
    // --- Phương thức xử lý submit form chỉnh sửa sản phẩm ---
    @PostMapping("/edit/{id}") // URL: /products/edit/{id} (POST request)
    public String updateProduct(@PathVariable("id") int id, @ModelAttribute("productForm") ProductFormDTO productForm, RedirectAttributes redirectAttributes) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tìm thấy để cập nhật.");
                return "redirect:/inventory";
            }
            Product existingProduct = optionalProduct.get();

            // Cập nhật thông tin sản phẩm
            Optional<Category> optionalCategory = categoryRepository.findById(productForm.getCategoryId());
            if (optionalCategory.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Danh mục không tồn tại.");
                return "redirect:/products/edit/" + id;
            }
            existingProduct.setCategory(optionalCategory.get());
            existingProduct.setTitle(productForm.getTitle());
            existingProduct.setPrice(productForm.getPrice());
            existingProduct.setDiscount(productForm.getDiscount());
            existingProduct.setThumbnail(productForm.getThumbnail());
            existingProduct.setDescription(productForm.getDescription());
            // deleted không thay đổi qua form này, nếu muốn thì phải thêm input riêng

            productRepository.save(existingProduct); // Lưu thay đổi sản phẩm

            // Cập nhật hoặc thêm mới thông tin tồn kho
            if (productForm.getInitialInventories() != null) {
                for (InitialInventoryItemDTO item : productForm.getInitialInventories()) {
                    Optional<Store> optionalStore = storeRepository.findById(item.getStoreId());
                    if (optionalStore.isPresent()) {
                        Store store = optionalStore.get();
                        // Tìm kiếm tồn kho hiện có cho sản phẩm và cửa hàng này
                        Optional<Inventory> existingInventory = inventoryRepository.findByProductAndStore(existingProduct, store);

                        Inventory inventory;
                        if (existingInventory.isPresent()) {
                            inventory = existingInventory.get();
                            inventory.setQuantity(item.getQuantity() != null ? item.getQuantity() : 0);
                        } else {
                            // Nếu chưa có tồn kho cho sản phẩm này tại kho này, tạo mới
                            inventory = new Inventory();
                            inventory.setProduct(existingProduct);
                            inventory.setStore(store);
                            inventory.setQuantity(item.getQuantity() != null ? item.getQuantity() : 0);
                        }
                        inventoryRepository.save(inventory); // Lưu hoặc cập nhật tồn kho
                    } else {
                        System.err.println("Cửa hàng không tồn tại với ID: " + item.getStoreId());
                    }
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được cập nhật thành công!");
            return "redirect:/inventory"; // Chuyển hướng về trang quản lý kho
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi cập nhật sản phẩm: " + e.getMessage());
            return "redirect:/products/edit/" + id;
        }
    }
}