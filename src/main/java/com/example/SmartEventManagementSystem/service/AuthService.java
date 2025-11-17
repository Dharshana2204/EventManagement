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

    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    private static final String PHONE_REGEX = "^\\d{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    // ------------------ REGISTER ---------------------
    public ApiResponse register(RegisterRequest request) {

        String name = request.getName() != null ? request.getName().trim() : "";
        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";
        String phoneNumber = request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : "";

        if (name.isEmpty()) {
            return new ApiResponse("Error", "Username is required", null, null);
        }

        if (email.isEmpty()) {
            return new ApiResponse("Error", "Email is required", null, null);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ApiResponse("Error", "Email format is invalid", null, null);
        }

        if (userRepository.existsByEmail(email)) {
            return new ApiResponse("Error", "Email already exists", null, null);
        }

        if (phoneNumber.isEmpty()) {
            return new ApiResponse("Error", "Phone number is required", null, null);
        }

        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return new ApiResponse("Error", "Phone number must be exactly 10 digits", null, null);
        }

        if (password.isEmpty()) {
            return new ApiResponse("Error", "Password is required", null, null);
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return new ApiResponse("Error",
                    "Password must contain at least one capital letter, one small letter, one number, and one special character",
                    null, null);
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);

        userRepository.save(user);

        return new ApiResponse("Success", "User created successfully", name, email);
    }


    // ------------------ LOGIN ---------------------
    public ApiResponse login(LoginRequest request) {

        String email = request.getEmail() != null ? request.getEmail().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";

        if (email.isEmpty()) {
            return new ApiResponse("Error", "Email is required", null, null);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ApiResponse("Error", "Email format is invalid", null, null);
        }

        if (password.isEmpty()) {
            return new ApiResponse("Error", "Password is required", null, null);
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return new ApiResponse("Error", "Invalid email or password", null, null);
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(password)) {
            return new ApiResponse("Error", "Invalid email or password", null, null);
        }

        return new ApiResponse("Success", "Login successful", user.getName(), user.getEmail());
    }
}
