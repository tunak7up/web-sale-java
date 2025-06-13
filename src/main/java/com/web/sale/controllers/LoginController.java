package com.web.sale.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login") // Xử lý yêu cầu GET đến /login để hiển thị trang đăng nhập
    public String showLoginForm() {
        return "login"; // Trả về tên của template login.html
    }
    // Bạn không cần phương thức POST ở đây vì Spring Security tự động xử lý POST /login
}