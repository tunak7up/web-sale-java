package com.web.sale.dto;

public class UserRegistrationDto {
    private String name;
    private String email;
    private String password;
    private String phone; // <-- THÊM TRƯỜNG NÀY

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String name, String email, String password, String phone) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone; // <-- CẬP NHẬT CONSTRUCTOR
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() { // <-- THÊM GETTER
        return phone;
    }
    public void setPhone(String phone) { // <-- THÊM SETTER
        this.phone = phone;
    }
}