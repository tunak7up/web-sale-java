package com.web.sale.controllers;

import com.web.sale.dto.CartItemDTO;
import com.web.sale.models.Product;
import com.web.sale.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    // Hiển thị trang giỏ hàng
    @GetMapping("")
    public String viewCart(HttpSession session, Model model) {
        Map<Integer, CartItemDTO> cart = (Map<Integer, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        model.addAttribute("cartItems", cart.values());
        model.addAttribute("cartTotal", calculateCartTotal(cart));
        return "cart"; // Trả về cart.html
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") int productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Map<Integer, CartItemDTO> cart = (Map<Integer, CartItemDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty() || productOptional.get().getDeleted() == 1) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tồn tại hoặc đã bị xóa.");
            return "redirect:/"; // Quay lại trang chủ hoặc trang chi tiết sản phẩm
        }

        Product product = productOptional.get();
        // Lấy giá hiện tại đã áp dụng giảm giá
        double currentPrice = product.getPrice() * (1 - (product.getDiscount() / 100.0));

        if (cart.containsKey(productId)) {
            CartItemDTO existingItem = cart.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setSubtotal(existingItem.getQuantity() * currentPrice);
        } else {
            CartItemDTO newItem = new CartItemDTO(
                    product.getId(),
                    product.getTitle(),
                    product.getThumbnail(),
                    currentPrice,
                    quantity
            );
            cart.put(productId, newItem);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào giỏ hàng.");
        return "redirect:/cart"; // Chuyển hướng về trang giỏ hàng
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PostMapping("/update")
    public String updateCart(@RequestParam("productId") int productId,
                             @RequestParam("quantity") int quantity,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Map<Integer, CartItemDTO> cart = (Map<Integer, CartItemDTO>) session.getAttribute("cart");
        if (cart == null || !cart.containsKey(productId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không có trong giỏ hàng.");
            return "redirect:/cart";
        }

        if (quantity <= 0) {
            cart.remove(productId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm khỏi giỏ hàng.");
        } else {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isEmpty() || productOptional.get().getDeleted() == 1) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tồn tại hoặc đã bị xóa.");
                return "redirect:/cart";
            }
            Product product = productOptional.get();
            double currentPrice = product.getPrice() * (1 - (product.getDiscount() / 100.0));

            CartItemDTO item = cart.get(productId);
            item.setQuantity(quantity);
            item.setSubtotal(quantity * currentPrice);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật giỏ hàng.");
        }
        return "redirect:/cart";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("productId") int productId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Map<Integer, CartItemDTO> cart = (Map<Integer, CartItemDTO>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(productId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm khỏi giỏ hàng.");
        }
        return "redirect:/cart";
    }

    // Tính tổng tiền giỏ hàng
    private Double calculateCartTotal(Map<Integer, CartItemDTO> cart) {
        return cart.values().stream()
                .mapToDouble(CartItemDTO::getSubtotal)
                .sum();
    }
}