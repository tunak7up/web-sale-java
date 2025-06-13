package com.web.sale.models;

import jakarta.persistence.*; // Sử dụng jakarta.persistence cho Spring Boot 3+
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Khớp với int trong DB

    @Column(name = "name", length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email; // Đã là NOT NULL và UNIQUE trong DB

    @Column(nullable = false, length = 64)
    private String password;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(length = 200)
    private String address;

    @Column // Ánh xạ tới cột 'deleted' int trong DB
    private Integer deleted;

    @CreationTimestamp // Tự động điền thời gian tạo
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Tự động cập nhật thời gian sửa đổi
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false) // Ánh xạ tới cột 'enabled' bit(1) NOT NULL trong DB
    private boolean enabled;

    // Các trường 'firstName', 'lastName' đã được loại bỏ theo yêu cầu của bạn

    // Trường 'verificationCode' đã được loại bỏ theo yêu cầu của bạn

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Constructors
    public User() {
        this.enabled = false; // Mặc định là false cho trạng thái ban đầu
        this.deleted = 0; // Giá trị mặc định cho soft-delete (nếu bạn sử dụng)
    }

    public User(String name, String email, String password, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.enabled = false;
        this.deleted = 0;
    }

    // --- Getters and Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}