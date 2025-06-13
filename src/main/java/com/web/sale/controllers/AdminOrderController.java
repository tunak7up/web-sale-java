package com.web.sale.controllers;

import com.web.sale.dto.OrderItemDTO;
import com.web.sale.dto.OrderSummaryDTO;
import com.web.sale.models.Order;
import com.web.sale.repository.OrderItemRepository;
import com.web.sale.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Hiển thị danh sách đơn hàng (trang quản lý đơn hàng)
    @GetMapping("")
    public String listOrders(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(defaultValue = "orderDate") String sortBy,
                             @RequestParam(defaultValue = "desc") String sortDir,
                             @RequestParam(required = false) String status, // Lọc theo trạng thái
                             @RequestParam(required = false) String search, // Tìm kiếm
                             Model model) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> orderPage;

        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("all")) {
            orderPage = orderRepository.findByStatus(status, pageable);
        } else {
            orderPage = orderRepository.findAll(pageable);
        }

        // TODO: Implement search logic if needed (findByReceiverNameContainingIgnoreCase or similar)
        // If search is implemented, it will need a custom query in OrderRepository

        List<OrderSummaryDTO> orderSummaryDTOs = orderPage.getContent().stream().map(order -> {
            OrderSummaryDTO dto = new OrderSummaryDTO();
            dto.setOrderId(order.getId());
            dto.setOrderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus());
            dto.setReceiverName(order.getReceiverName());
            dto.setPaymentMethod(order.getPaymentMethod());
            dto.setPaymentStatus(order.getPaymentStatus());
            // Có thể thêm địa chỉ rút gọn hoặc chi tiết hơn nếu cần
            dto.setShippingAddress(order.getShippingAddress() + ", " + order.getShippingDistrict() + ", " + order.getShippingCity());
            dto.setShippingPhone(order.getShippingPhone());
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("orders", orderSummaryDTOs);
        model.addAttribute("currentPage", orderPage.getNumber());
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentStatusFilter", status); // Để giữ trạng thái lọc trên view

        return "order-manage"; // Trả về order-manage.html
    }

    // Xem chi tiết đơn hàng (có thể tạo một trang riêng hoặc popup)
    // Ví dụ: /admin/orders/{id}
    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable int id, Model model) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Đơn hàng không tồn tại.");
        }

        Order order = orderOptional.get();
        OrderSummaryDTO orderDetail = new OrderSummaryDTO();
        orderDetail.setOrderId(order.getId());
        orderDetail.setOrderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        orderDetail.setTotalAmount(order.getTotalAmount());
        orderDetail.setStatus(order.getStatus());
        orderDetail.setShippingAddress(order.getShippingAddress());
        orderDetail.setShippingCity(order.getShippingCity());
        orderDetail.setShippingDistrict(order.getShippingDistrict());
        orderDetail.setShippingPhone(order.getShippingPhone());
        orderDetail.setReceiverName(order.getReceiverName());
        orderDetail.setPaymentMethod(order.getPaymentMethod());
        orderDetail.setPaymentStatus(order.getPaymentStatus());
        orderDetail.setNotes(order.getNotes());

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
        orderDetail.setItems(itemDTOs);

        model.addAttribute("orderDetail", orderDetail);
        return "order-detail-modal"; // Hoặc một template riêng cho chi tiết đơn hàng
    }

    // Cập nhật trạng thái đơn hàng (ví dụ: từ Pending -> Processing -> Shipped -> Delivered)
    @PostMapping("/{id}/update-status")
    public String updateOrderStatus(@PathVariable int id,
                                    @RequestParam("newStatus") String newStatus,
                                    RedirectAttributes redirectAttributes) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đơn hàng không tồn tại.");
            return "redirect:/admin/orders";
        }

        Order order = orderOptional.get();
        // Kiểm tra logic chuyển trạng thái (ví dụ: không cho phép chuyển từ Delivered sang Pending)
        // Đây là nơi bạn sẽ thêm business logic
        order.setStatus(newStatus);
        orderRepository.save(order);

        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái đơn hàng #" + id + " thành " + newStatus);
        return "redirect:/admin/orders";
    }

    // Cập nhật trạng thái thanh toán (ví dụ: từ Unpaid -> Paid)
    @PostMapping("/{id}/update-payment-status")
    public String updatePaymentStatus(@PathVariable int id,
                                      @RequestParam("newPaymentStatus") String newPaymentStatus,
                                      RedirectAttributes redirectAttributes) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đơn hàng không tồn tại.");
            return "redirect:/admin/orders";
        }

        Order order = orderOptional.get();
        order.setPaymentStatus(newPaymentStatus);
        orderRepository.save(order);

        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái thanh toán đơn hàng #" + id + " thành " + newPaymentStatus);
        return "redirect:/admin/orders";
    }
}