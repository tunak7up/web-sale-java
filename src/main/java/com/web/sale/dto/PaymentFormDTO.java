package com.web.sale.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PaymentFormDTO {
    @NotBlank(message = "Vui lòng chọn phương thức thanh toán")
    private String paymentMethod; // e.g., "COD", "Credit Card"

    // Thêm các trường này nếu bạn thực sự xử lý thanh toán bằng thẻ
    // @NotBlank(message = "Số thẻ không được để trống")
    // @Pattern(regexp = "\\d{16}", message = "Số thẻ không hợp lệ")
    // private String cardNumber;

    // @NotBlank(message = "Ngày hết hạn không được để trống")
    // @Pattern(regexp = "\\d{2}/\\d{2}", message = "Ngày hết hạn không hợp lệ (MM/YY)")
    // private String expirationDate;

    // @NotBlank(message = "CVV không được để trống")
    // @Pattern(regexp = "\\d{3}", message = "CVV không hợp lệ")
    // private String cvv;

    // @NotBlank(message = "Địa chỉ thanh toán không được để trống")
    // private String billingAddress;

    // Constructors, Getters, Setters

    public PaymentFormDTO() {
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}