package com.example.SmartEventManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String status;
    private String message;
    private String name;
    private String email;
    private Object data;
    private String token;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;

    }

    public ApiResponse(String status, String message, Object data, String token) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public boolean isSuccess() {
        return "Success".equalsIgnoreCase(this.status);
    }
}
