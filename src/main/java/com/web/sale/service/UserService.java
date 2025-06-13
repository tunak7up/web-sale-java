package com.web.sale.service;

import com.web.sale.dto.UserRegistrationDto;
import com.web.sale.models.User;

public interface UserService {
    // Phương thức để lưu người dùng mới (đăng ký)
    User save(UserRegistrationDto registrationDto);

    // Phương thức để tìm người dùng theo email (hữu ích cho đăng nhập và kiểm tra trùng lặp)
    User findByEmail(String email);
}