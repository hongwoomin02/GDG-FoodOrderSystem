package com.example.foodordersystem.Service;

import com.example.foodordersystem.DTO.User.UserRequestDTO;
import com.example.foodordersystem.DTO.User.UserResponseDTO;
import com.example.foodordersystem.Entity.User;
import com.example.foodordersystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO addUser(UserRequestDTO request) {
        User user = new User(request.name());
        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getName());
    }
}
