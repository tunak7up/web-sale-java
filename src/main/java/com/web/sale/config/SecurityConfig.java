//package com.web.sale.config;
//
//// import com.web.sale.service.UserService; // Dòng này có thể không còn cần thiết ở đây,
//// hoặc bạn có thể giữ lại nếu dùng UserService ở nơi khác trong SecurityConfig
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.core.userdetails.UserDetailsService; // <-- THÊM IMPORT NÀY
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private UserDetailsService userDetailsService; // <-- THAY ĐỔI KIỂU TỪ UserService SANG UserDetailsService
//
//    @Autowired // @Autowired là tùy chọn ở đây nhưng giữ lại cũng không sao
//    public SecurityConfig(UserDetailsService userDetailsService) { // <-- THAY ĐỔI KIỂU Ở ĐÂY CŨNG
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // *** CẢNH BÁO: KHÔNG SỬ DỤNG TRONG MÔI TRƯỜNG SẢN XUẤT! ***
//        return NoOpPasswordEncoder.getInstance();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//        auth.setUserDetailsService(userDetailsService); // <-- LỖI SẼ BIẾN MẤT Ở ĐÂY
//        auth.setPasswordEncoder(passwordEncoder());
//        return auth;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/registration**", "/js/**", "/css/**", "/img/**").permitAll()
//                        .requestMatchers("/login**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .invalidateHttpSession(true)
//                        .clearAuthentication(true)
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
//}

package com.web.sale.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder; // CẢNH BÁO: KHÔNG DÙNG TRONG PRODUCTION
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // *** CẢNH BÁO: KHÔNG SỬ DỤNG NoOpPasswordEncoder TRONG MÔI TRƯỜNG SẢN XUẤT! ***
        // Đây chỉ là để đơn giản hóa việc test. Trong thực tế, hãy dùng BCryptPasswordEncoder hoặc tương tự.
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/registration**", "/js/**", "/css/**", "/img/**").permitAll()
                        .requestMatchers("/login**").permitAll()
                        // *** THÊM DÒNG NÀY ĐỂ CHO PHÉP TRUY CẬP '/inventory/adjust' CHO MỌI NGƯỜI ***
                        .requestMatchers("/inventory/adjust").permitAll() // Tạm thời cho phép mọi người truy cập
                        .anyRequest().authenticated() // Mọi request khác đều yêu cầu xác thực
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // *** TẠM THỜI VÔ HIỆU HÓA CSRF ĐỂ ĐƠN GIẢN HÓA VIỆC TEST CÁC REQUEST POST ***
                // CẢNH BÁO: KHÔNG NÊN VÔ HIỆU HÓA CSRF TRONG MÔI TRƯỜNG SẢN XUẤT VÌ NÓ TẠO LỖ HỔNG BẢO MẬT.
                // Khi triển khai, bạn cần bật lại CSRF và thêm CSRF token vào các request POST của bạn.
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

}