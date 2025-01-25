package com.example.foodordersystem.IntergrationTest;

import com.example.foodordersystem.DTO.User.LoginRequestDTO;
import com.example.foodordersystem.DTO.User.UserRequestDTO;
import com.example.foodordersystem.Entity.User;
import com.example.foodordersystem.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 테스트 후 데이터가 롤백되도록 설정
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository; // 실제 DB를 확인하기 위해 사용

    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSignup() throws Exception {
        // Given
        UserRequestDTO userRequestDTO = new UserRequestDTO("hong@naver.com", "1111", "h");

        // When & Then
        mockMvc.perform(post("/users/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.name").value("John"))
                .andDo(print());

        // 실제 DB에 저장되었는지 확인
        User savedUser = userRepository.findByEmail("test@email.com").orElseThrow();
        assertThat(savedUser.getName()).isEqualTo("John");
    }


    @Test
    void testLogin() throws Exception {
        // Given
        UserRequestDTO userRequestDTO = new UserRequestDTO("test@email.com", "password", "John");
        userRepository.save(new User("test@email.com", passwordEncoder.encode("password"), "John")); // 비밀번호 암호화

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("test@email.com", "password");

        // When & Then
        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("로그인 성공"))
                .andDo(print());
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        // Given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("h@email.com", "wrongpassword");

        // When & Then
        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    void testLogout() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        User user = new User("test@email.com", passwordEncoder.encode("password"), "hong");
        userRepository.save(user);

        // 로그인 요청으로 세션 생성
        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@email.com\", \"password\": \"password\"}")
                        .session(session))
                .andExpect(status().isOk());

        // When & Then: 로그아웃 요청
        mockMvc.perform(post("/users/logout")
                        .with(csrf())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("로그아웃 성공"))
                .andDo(print());
    }

}
