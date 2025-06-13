package com.web.sale.controllers;

import com.web.sale.dto.CartItemDTO;
import com.web.sale.dto.CheckoutFormDTO;
import com.web.sale.repository.OrderRepository;
import com.web.sale.repository.OrderItemRepository;
import com.web.sale.repository.ProductRepository;
import com.web.sale.repository.InventoryRepository; // Để cập nhật tồn kho
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository; // Để cập nhật tồn kho

    // Hiển thị trang thông tin giao hàng
    @GetMapping("")
    public String showCheckoutForm(HttpSession session, Model model) {
        Map<Integer, CartItemDTO> cart = (Map<Integer, CartItemDTO>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            // Nếu giỏ hàng trống, chuyển hướng về trang giỏ hàng hoặc thông báo
            model.addAttribute("errorMessage", "Giỏ hàng của bạn đang trống.");
            return "redirect:/cart";
        }

        model.addAttribute("checkoutForm", new CheckoutFormDTO());
        model.addAttribute("cartItems", cart.values());
        model.addAttribute("cartTotal", calculateCartTotal(cart));
        return "checkout"; // Trả về checkout.html
    }

    // Xử lý thông tin giao hàng và chuyển đến trang thanh toán
    @PostMapping("/process-shipping")
    public String processShipping(@Valid @ModelAttribute("checkoutForm") CheckoutFormDTO checkoutForm,
                                  BindingResult result,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        Map<Integer, CartItemDTO> cart = (Map<Integer, CartItemDTO>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giỏ hàng của bạn đang trống.");
            return "redirect:/cart";
        }

        if (result.hasErrors()) {
            // Nếu có lỗi validation, quay lại form và hiển thị lỗi
            model.addAttribute("cartItems", cart.values());
            model.addAttribute("cartTotal", calculateCartTotal(cart));
            return "checkout";
        }

        // Lưu thông tin checkout vào session để sử dụng ở bước thanh toán
        session.setAttribute("checkoutForm", checkoutForm);

        // Chuyển hướng đến trang thanh toán
        return "redirect:/payment";
    }

    private Double calculateCartTotal(Map<Integer, CartItemDTO> cart) {
        return cart.values().stream()
                .mapToDouble(CartItemDTO::getSubtotal)
                .sum();
    }
}