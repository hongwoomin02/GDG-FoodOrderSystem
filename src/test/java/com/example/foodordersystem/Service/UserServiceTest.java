package com.example.foodordersystem.Service;

import com.example.foodordersystem.DTO.User.UserRequestDTO;
import com.example.foodordersystem.DTO.User.UserResponseDTO;
import com.example.foodordersystem.Entity.User;
import com.example.foodordersystem.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 가입테스트")
    void testSignup() throws IllegalAccessException, NoSuchFieldException {
        // Given
        UserRequestDTO request = new UserRequestDTO("h@.com", "1234", "h");
        String encodedPassword = passwordEncoder.encode("1234");

        Mockito.when(userRepository.existsByEmail(request.email())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);

        User givenUser = new User(request.email(), encodedPassword, request.name());

        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true); // private 필드에 접근할 수 있도록 설정
        idField.set(givenUser, 1L);  // id 값을 1L로 설정

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(givenUser);

        // When
        UserResponseDTO response = userService.signup(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("h@.com");
        assertThat(response.name()).isEqualTo("h");
        assertThat(response.id()).isEqualTo(1L);

        Mockito.verify(userRepository).existsByEmail(request.email());
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("회원 가입 실패/중복 이메일")
    void testSignupDuplicateEmail() {
        // Given
        UserRequestDTO request = new UserRequestDTO("h@.com", "1234", "hong");

        Mockito.when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.signup(request));
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");

        Mockito.verify(userRepository).existsByEmail(request.email());
        Mockito.verifyNoInteractions(passwordEncoder);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("로그인 성공")
    void testLoginSuccess() {
        // Given
        String email = "h@nave.com";
        String rawPassword = "1234";
        String encodedPassword =  passwordEncoder.encode(rawPassword);

        User user = new User(email, encodedPassword, "hong");
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // When
        userService.login(email, rawPassword);

        // Then
        Mockito.verify(userRepository).findByEmail(email);
        Mockito.verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }

    @Test
    @DisplayName("로그인/존재하지 않는 이메일")
    void testLoginWrongEmail() {
        // Given
        String email = "h@nave.com";
        String rawPassword = "1234";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.login(email, rawPassword));
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 이메일입니다.");

        // Then
        Mockito.verify(userRepository).findByEmail(email);
        Mockito.verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("로그인실패/비밀번호 불일치")
    void testLoginWrongPassword() {
        // Given
        String email = "h@nave.com";
        String rawPassword = "1234";
        String encodedPassword = "wrongPassword";

        User user = new User(email, encodedPassword, "hong");
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.login(email, rawPassword));
        assertThat(exception.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다.");

        // Then
        Mockito.verify(userRepository).findByEmail(email);
        Mockito.verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }
}
