package com.example.foodordersystem.Controller;

import com.example.foodordersystem.DTO.User.LoginRequestDTO;
import com.example.foodordersystem.DTO.User.UserRequestDTO;
import com.example.foodordersystem.DTO.User.UserResponseDTO;
import com.example.foodordersystem.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final HttpSession httpSession;

    public UserController(UserService userService, HttpSession httpSession) {
        this.userService = userService;
        this.httpSession = httpSession;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO response = userService.signup(userRequestDTO);
        httpSession.setAttribute("email", userRequestDTO.email());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        userService.login(loginRequestDTO.email(), loginRequestDTO.password());
        httpSession.setAttribute("email", loginRequestDTO.email());
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        httpSession.invalidate();
        return ResponseEntity.ok("로그아웃 성공");
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        if (httpSession.getAttribute("email") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }
        return ResponseEntity.ok("인증된 사용자입니다");
    }
}