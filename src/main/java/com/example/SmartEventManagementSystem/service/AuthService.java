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
import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$");
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public ApiResponse register(RegisterRequest request) {

        String name = request.getName() != null ? request.getName() : "";
        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";
        String phone = request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : "";

        if (name.isEmpty())
            return new ApiResponse("Error", "Name is required");

        if (email.isEmpty())
            return new ApiResponse("Error", "Email is required");

        if (!EMAIL_PATTERN.matcher(email).matches())
            return new ApiResponse("Error", "Invalid email format");

        if (userRepository.existsByEmail(email))
            return new ApiResponse("Error", "Email already exists");

        if (userRepository.existsByName(name))
            return new ApiResponse("Error", "Username already exists");

        if (phone.isEmpty())
            return new ApiResponse("Error", "Phone number is required");

        if (!PHONE_PATTERN.matcher(phone).matches())
            return new ApiResponse("Error", "Phone number must be 10 digits");

        if (password.isEmpty())
            return new ApiResponse("Error", "Password is required");

        if (!PASSWORD_PATTERN.matcher(password).matches())
            return new ApiResponse("Error",
                    "Password must contain 1 uppercase, 1 lowercase, 1 digit, 1 special character, min 8 chars");

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return new ApiResponse("Success", "User registered successfully");
    }

    public ApiResponse login(LoginRequest request) {

        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";

        if (email.isEmpty())
            return new ApiResponse("Error", "Email is required");

        if (!EMAIL_PATTERN.matcher(email).matches())
            return new ApiResponse("Error", "Invalid email format");

        if (password.isEmpty())
            return new ApiResponse("Error", "Password is required");

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty())
            return new ApiResponse("Error", "Invalid email or password");

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword()))
            return new ApiResponse("Error", "Invalid email or password");

        String token = jwtUtil.generateToken(user.getEmail());

        ApiResponse response = new ApiResponse();
        response.setStatus("Success");
        response.setMessage("Login successful");
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setToken(token);

        return response;
    }
}
