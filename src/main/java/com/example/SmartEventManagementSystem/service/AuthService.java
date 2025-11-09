package com.example.SmartEventManagementSystem.service;

import com.example.SmartEventManagementSystem.dto.ApiResponse;
import com.example.SmartEventManagementSystem.dto.LoginRequest;
import com.example.SmartEventManagementSystem.dto.RegisterRequest;
import com.example.SmartEventManagementSystem.entity.User;
import com.example.SmartEventManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Password must contain: at least one capital letter, one small letter, one special char, one number
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    private static final String PHONE_REGEX = "^\\d{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    public ApiResponse register(RegisterRequest request) {
        // Trim all fields
        String name = request.getName() != null ? request.getName().trim() : "";
        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";
        String phoneNumber = request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : "";

        // Validate username (name) - must be unique and not empty
        if (name.isEmpty()) {
            return new ApiResponse("Error", "Username is required");
        }

        if (userRepository.existsByName(name)) {
            return new ApiResponse("Error", "Username already exists");
        }

        // Validate email - must match regex and not exist in DB
        if (email.isEmpty()) {
            return new ApiResponse("Error", "Email is required");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ApiResponse("Error", "Email format is invalid");
        }

        if (userRepository.existsByEmail(email)) {
            return new ApiResponse("Error", "Email already exists");
        }

        // Validate phone number - must be exactly 10 digits
        if (phoneNumber.isEmpty()) {
            return new ApiResponse("Error", "Phone number is required");
        }

        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return new ApiResponse("Error", "Phone number must be exactly 10 digits");
        }

        // Validate password - must contain one capital letter, one small letter, one special char, one number
        if (password.isEmpty()) {
            return new ApiResponse("Error", "Password is required");
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return new ApiResponse("Error", "Password must contain at least one capital letter, one small letter, one special character, and one number");
        }

        // All validations passed, create user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // In production, use password encoding (BCrypt)
        user.setPhoneNumber(phoneNumber);

        userRepository.save(user);

        return new ApiResponse("Success", "User created successfully");
    }

    public ApiResponse login(LoginRequest request) {
        // Trim fields
        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";

        // Validate email format
        if (email.isEmpty()) {
            return new ApiResponse("Error", "Email is required");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ApiResponse("Error", "Email format is invalid");
        }

        // Validate password
        if (password.isEmpty()) {
            return new ApiResponse("Error", "Password is required");
        }

        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return new ApiResponse("Error", "Invalid email or password");
        }

        User user = userOptional.get();

        // Check if password matches
        if (!user.getPassword().equals(password)) {
            return new ApiResponse("Error", "Invalid email or password");
        }

        return new ApiResponse("Success", "Login successful", user.getName());
    }
}
