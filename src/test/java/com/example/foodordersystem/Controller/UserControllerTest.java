package com.example.foodordersystem.Controller;

import com.example.foodordersystem.DTO.User.LoginRequestDTO;
import com.example.foodordersystem.DTO.User.UserRequestDTO;
import com.example.foodordersystem.DTO.User.UserResponseDTO;
import com.example.foodordersystem.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = true)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("회원가입 테스트")
    void testSignup() throws Exception {
        //Given
        UserRequestDTO request = new UserRequestDTO("hong@naver.com", "1234", "hong");
        UserResponseDTO mockResponse = new UserResponseDTO(1L, "hong@naver.com", "hong");

        when(userService.signup(any(UserRequestDTO.class))).thenReturn(mockResponse);

        //When
        mockMvc.perform(post("/users/signup")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("hong@naver.com"))
                .andExpect(jsonPath("$.name").value("hong"))
                .andDo(print());

        verify(userService).signup(any(UserRequestDTO.class));
    }

    @Test
    @WithMockUser
    @DisplayName("로그인 테스트")
    void testLogin() throws Exception {
        // Given
        doNothing().when(userService).login(anyString(), anyString());

        // When
        mockMvc.perform(post("/users/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"email\": \"hong@naver.com\", \"password\": \"1234\"}"))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().string("로그인 성공"))
                .andDo(print());

        verify(userService).login(anyString(), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃 테스트")
    void testLogout() throws Exception {
        // Given
        MockHttpSession mockHttpSession = new MockHttpSession();

        // When
        mockMvc.perform(post("/users/logout")
                        .session(mockHttpSession)
                        .with(csrf()))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().string("로그아웃 성공"))
                .andDo(print());

        assertTrue(mockHttpSession.isInvalid());
    }


    @Test
    @WithMockUser
    @DisplayName("인증된 사용자가 요청보낼때 엔드포인트 올바른 응답 반환 확인")
    void testTestEndpointWithAuthenticatedUser() throws Exception {

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("email", "hong@naver.com");

        mockMvc.perform(get("/users/test")
                        .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(content().string("인증된 사용자입니다: hong@naver.com"))
                .andDo(print());
    }


    @Test
    @DisplayName("인증되지 않은 사용자가 요청보낼때 엔드포인트 응답 반환 확인")
    void testTestEndpointWithoutAuthenticatedUser() throws Exception {
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("email", null);

        mockMvc.perform(get("/users/test").session(mockSession))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("로그인 필요"))
                .andDo(print());
    }


}