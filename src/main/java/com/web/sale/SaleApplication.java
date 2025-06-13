package com.web.sale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.web.sale.controllers",
		"com.web.sale.repository", // Có thể cái này là dư nếu bạn đã dùng repositories
		"com.web.sale.service",
		"com.web.sale.config"// <-- THÊM GÓI NÀY VÀO!
})public class SaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaleApplication.class, args);
	}

}
