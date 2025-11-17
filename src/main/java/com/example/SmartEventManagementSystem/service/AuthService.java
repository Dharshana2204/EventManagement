package com.example.SmartEventManagementSystem.service;

import com.example.SmartEventManagementSystem.dto.ApiResponse;
import com.example.SmartEventManagementSystem.dto.LoginRequest;
import com.example.SmartEventManagementSystem.dto.RegisterRequest;
import com.example.SmartEventManagementSystem.entity.User;
import com.example.SmartEventManagementSystem.repository.UserRepository;
import com.example.SmartEventManagementSystem.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public JwtUtil jwtUtil;


    public ApiResponse login(LoginRequest request) {

        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";

        if (email.isEmpty()) {
            return new ApiResponse("Error", "Email is required");
        }

        if (password.isEmpty()) {
            return new ApiResponse("Error", "Password is required");
        }

        Optional<User> userOp = userRepository.findByEmail(email);

        if (userOp.isEmpty()) {
            return new ApiResponse("Error", "Invalid email or password");
        }

        User user = userOp.get();


        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new ApiResponse("Error", "Invalid email or password");
        }


        String token = jwtUtil.generateToken(user.getEmail());


        return new ApiResponse("Success", "Login successful",token,  user.getName());
    }

    public ApiResponse register(RegisterRequest request) {

        String name = request.getName().trim();
        String email = request.getEmail().trim();
        String password = request.getPassword().trim();
        String phone = request.getPhoneNumber().trim();

        if (name.isEmpty())
            return new ApiResponse("Error", "Name is required");

        if (email.isEmpty())
            return new ApiResponse("Error", "Email is required");

        if (password.isEmpty())
            return new ApiResponse("Error", "Password is required");

        if (phone.isEmpty())
            return new ApiResponse("Error", "Phone number is required");

        if (userRepository.existsByEmail(email))
            return new ApiResponse("Error", "Email already exists");

        if (userRepository.existsByName(name))
            return new ApiResponse("Error", "Username already exists");


        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phone);

        userRepository.save(user);

        return new ApiResponse("Success", "User registered successfully");
    }
}
