package com.web.sale.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") // Đặt prefix cho tất cả các đường dẫn admin
public class AdminController {

    @GetMapping
    public String adminDashboard() {
        return "admin/dashboard"; // Trả về file dashboard.html trong thư mục templates/admin/
    }

//    @GetMapping("/orders") // Xử lý yêu cầu GET cho /admin/orders
//    public String orderManagement() {
//        return "admin/order-manage"; // Trả về file order-manage.html trong templates/admin/
//    }

    @GetMapping("/tech-tasks")
    public String techTasks() {
        return "admin/tech-task";
    }

    @GetMapping("/users")
    public String userManagement() {
        return "admin/user-management";
    }

    // Các phương thức khác cho admin sau này...
}