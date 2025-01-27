package com.example.foodordersystem.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf
//                        .requireCsrfProtectionMatcher(
//                                request -> !request.getMethod().equals("GET")  // GET 요청 외에는 모두 CSRF 보호
//                        )
//                )
                .csrf(csrf->csrf.disable()) //포스트맨으로 요청 테스트시 활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/signup", "/users/login").permitAll() // 로그인과 회원가입은 인증 없이 접근 허용
                        .requestMatchers("/test").authenticated()
                        .requestMatchers("/users/logout").authenticated()  // 로그아웃은 인증된 사용자만 접근
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // HTTP 200 상태 반환
                            response.setContentType("text/plain;charset=UTF-8");
                            response.getWriter().write("로그아웃 성공");
                        })
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
