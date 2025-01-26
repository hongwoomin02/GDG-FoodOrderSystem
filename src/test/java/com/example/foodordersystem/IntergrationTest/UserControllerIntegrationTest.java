package com.example.foodordersystem.IntergrationTest;

import com.example.foodordersystem.DTO.User.LoginRequestDTO;
import com.example.foodordersystem.DTO.User.UserRequestDTO;
import com.example.foodordersystem.Entity.User;
import com.example.foodordersystem.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
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
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 테스트")
    void testSignup() throws Exception {
        // Given
        UserRequestDTO userRequestDTO = new UserRequestDTO("hong@naver.com", "1111", "h");

        // When
        mockMvc.perform(post("/users/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("hong@naver.com"))
                .andExpect(jsonPath("$.name").value("h"))
                .andDo(print());

        User savedUser = userRepository.findByEmail("test@email.com").orElseThrow();
        assertThat(savedUser.getName()).isEqualTo("John");
    }


    @Test
    @DisplayName("로그인 테스트")
    void testLogin() throws Exception {
        // Given
        UserRequestDTO userRequestDTO = new UserRequestDTO("hong@naver.com", "1111", "h");
        userRepository.save(new User("hong@naver.com", passwordEncoder.encode("1111"), "h"));

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("hong@naver.com", "1111");

        // When
        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().string("로그인 성공"))
                .andDo(print());
    }

    @Test
    @DisplayName("잘못된 정보로 로그인시도")
    void testLoginWrongInfo() throws Exception {
        // Given
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("Wrong@email.com", "wrongpassword");

        // When
        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                // Then
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @DisplayName("로그아웃 테스트")
    void testLogout() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        User user = new User("hong@naver.com", passwordEncoder.encode("1111"), "hong");
        userRepository.save(user);

        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"hong@naver.com\", \"password\": \"1111\"}")
                        .session(session))
                .andExpect(status().isOk());

        // When
        mockMvc.perform(post("/users/logout")
                        .with(csrf())
                        .session(session))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().string("로그아웃 성공"))
                .andDo(print());
    }

}
