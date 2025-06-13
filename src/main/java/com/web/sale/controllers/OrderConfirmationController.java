package com.web.sale.controllers;

import com.web.sale.dto.OrderItemDTO;
import com.web.sale.dto.OrderSummaryDTO;
import com.web.sale.models.Order;
import com.web.sale.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class OrderConfirmationController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/order-confirmation")
    public String showOrderConfirmation(Model model, RedirectAttributes redirectAttributes) {
        Integer orderId = (Integer) model.asMap().get("orderId"); // Lấy orderId từ redirectAttributes

        if (orderId == null) {
            // Nếu không có orderId, có thể chuyển hướng về trang chủ hoặc thông báo lỗi
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thông tin đơn hàng.");
            return "redirect:/";
        }

        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đơn hàng không tồn tại.");
            return "redirect:/";
        }

        Order order = orderOptional.get();
        OrderSummaryDTO orderSummary = new OrderSummaryDTO();
        orderSummary.setOrderId(order.getId());
        orderSummary.setOrderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        orderSummary.setTotalAmount(order.getTotalAmount());
        orderSummary.setStatus(order.getStatus());
        orderSummary.setShippingAddress(order.getShippingAddress());
        orderSummary.setShippingCity(order.getShippingCity());
        orderSummary.setShippingDistrict(order.getShippingDistrict());
        orderSummary.setShippingPhone(order.getShippingPhone());
        orderSummary.setReceiverName(order.getReceiverName());
        orderSummary.setPaymentMethod(order.getPaymentMethod());
        orderSummary.setPaymentStatus(order.getPaymentStatus());
        orderSummary.setNotes(order.getNotes());

        // Chuyển đổi OrderItem thành OrderItemDTO
        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getTitle());
            itemDTO.setProductThumbnail(item.getProduct().getThumbnail());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPriceAtOrder(item.getPriceAtOrder());
            itemDTO.setSubtotal(item.getPriceAtOrder() * item.getQuantity());
            return itemDTO;
        }).collect(Collectors.toList());
        orderSummary.setItems(itemDTOs);

        model.addAttribute("orderSummary", orderSummary);
        return "order-confirmation"; // Trả về order-confirmation.html
    }
}