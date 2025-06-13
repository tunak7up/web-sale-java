package com.web.sale.controllers;

import com.web.sale.dto.CartItemDTO;
import com.web.sale.dto.CheckoutFormDTO;
import com.web.sale.dto.PaymentFormDTO;
import com.web.sale.models.Order;
import com.web.sale.models.OrderItem;
import com.web.sale.models.Product;
import com.web.sale.models.Inventory;
import com.web.sale.repository.OrderRepository;
import com.web.sale.repository.OrderItemRepository;
import com.web.sale.repository.ProductRepository;
import com.web.sale.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    // Hiển thị trang thanh toán
    @GetMapping("")
    public String showPaymentForm(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Map<Integer, CartItemDTO> cart = (Map<Integer, CartItemDTO>) session.getAttribute("cart");
        CheckoutFormDTO checkoutForm = (CheckoutFormDTO) session.getAttribute("checkoutForm");

        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giỏ hàng của bạn đang trống.");
            return "redirect:/cart";
        }
        if (checkoutForm == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng nhập thông tin giao hàng trước.");
            return "redirect:/checkout";
        }

        model.addAttribute("paymentForm", new PaymentFormDTO());
        model.addAttribute("cartItems", cart.values());
        model.addAttribute("checkoutForm", checkoutForm); // Để hiển thị lại thông tin shipping
        model.addAttribute("cartTotal", calculateCartTotal(cart));
        return "payment"; // Trả về payment.html
    }

    // Xử lý thanh toán và tạo đơn hàng
    @PostMapping("/place-order")
    public String placeOrder(@Valid @ModelAttribute("paymentForm") PaymentFormDTO paymentForm,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        Map<Integer, CartItemDTO> cart = (Map<Integer, CartItemDTO>) session.getAttribute("cart");
        CheckoutFormDTO checkoutForm = (CheckoutFormDTO) session.getAttribute("checkoutForm");

        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giỏ hàng của bạn đang trống.");
            return "redirect:/cart";
        }
        if (checkoutForm == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng nhập thông tin giao hàng trước.");
            return "redirect:/checkout";
        }

        if (result.hasErrors()) {
            // Nếu có lỗi validation, quay lại form và hiển thị lỗi
            model.addAttribute("cartItems", cart.values());
            model.addAttribute("checkoutForm", checkoutForm);
            model.addAttribute("cartTotal", calculateCartTotal(cart));
            return "payment";
        }

        // --- Kiểm tra tồn kho trước khi đặt hàng ---
        for (CartItemDTO item : cart.values()) {
            Optional<Product> productOptional = productRepository.findById(item.getProductId());
            if (productOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm '" + item.getProductName() + "' không còn tồn tại.");
                session.removeAttribute("cart"); // Xóa giỏ hàng để tránh lỗi
                return "redirect:/cart";
            }
            Product product = productOptional.get();
            List<Inventory> inventories = inventoryRepository.findByProduct(product);
            int availableStock = inventories.stream().mapToInt(Inventory::getQuantity).sum();

            if (item.getQuantity() > availableStock) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm '" + item.getProductName() + "' không đủ số lượng trong kho. Chỉ còn " + availableStock + " sản phẩm.");
                return "redirect:/cart"; // Quay lại giỏ hàng để người dùng điều chỉnh
            }
        }

        // --- Tạo đơn hàng (Order) ---
        Order order = new Order();
        // Giả sử có user đăng nhập, nếu không thì user_id sẽ là NULL
        // Lấy User từ session hoặc SecurityContextHolder nếu bạn đã tích hợp Spring Security
        // For now, let's assume no authenticated user, so user is null for guest checkout
        // User currentUser = (User) session.getAttribute("currentUser"); // Ví dụ nếu bạn lưu User vào session
        // order.setUser(currentUser);

        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(calculateCartTotal(cart));
        order.setStatus("Pending"); // Trạng thái ban đầu của đơn hàng
        order.setShippingAddress(checkoutForm.getShippingAddress());
        order.setShippingCity(checkoutForm.getShippingCity());
        order.setShippingDistrict(checkoutForm.getShippingDistrict());
        order.setShippingPhone(checkoutForm.getShippingPhone());
        order.setReceiverName(checkoutForm.getReceiverName());
        order.setPaymentMethod(paymentForm.getPaymentMethod());
        order.setPaymentStatus(paymentForm.getPaymentMethod().equals("COD") ? "Unpaid" : "Pending"); // Nếu COD thì Unpaid, còn lại là Pending cho các phương thức khác
        order.setNotes(checkoutForm.getNotes());

        // --- Lưu Order vào database ---
        Order savedOrder = orderRepository.save(order);

        // --- Tạo và lưu OrderItems ---
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDTO cartItem : cart.values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            Product product = productRepository.findById(cartItem.getProductId()).orElse(null);
            if (product != null) {
                orderItem.setProduct(product);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPriceAtOrder(cartItem.getPrice()); // Giá tại thời điểm đặt hàng
                orderItems.add(orderItem);
            }
        }
        orderItemRepository.saveAll(orderItems);

        // --- Cập nhật tồn kho ---
        for (CartItemDTO item : cart.values()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                List<Inventory> inventories = inventoryRepository.findByProduct(product);
                int remainingQuantityToDeduct = item.getQuantity();

                for (Inventory inv : inventories) {
                    if (remainingQuantityToDeduct <= 0) break;

                    if (inv.getQuantity() >= remainingQuantityToDeduct) {
                        inv.setQuantity(inv.getQuantity() - remainingQuantityToDeduct);
                        remainingQuantityToDeduct = 0;
                    } else {
                        remainingQuantityToDeduct -= inv.getQuantity();
                        inv.setQuantity(0);
                    }
                    inventoryRepository.save(inv);
                }
            }
        }

        // --- Dọn dẹp session và chuyển hướng đến trang xác nhận ---
        session.removeAttribute("cart");
        session.removeAttribute("checkoutForm");
        redirectAttributes.addFlashAttribute("orderId", savedOrder.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Đơn hàng của bạn đã được đặt thành công!");
        return "redirect:/order-confirmation";
    }

    private Double calculateCartTotal(Map<Integer, CartItemDTO> cart) {
        return cart.values().stream()
                .mapToDouble(CartItemDTO::getSubtotal)
                .sum();
    }
}