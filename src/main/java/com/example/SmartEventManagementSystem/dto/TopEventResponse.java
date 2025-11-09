package com.example.SmartEventManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopEventResponse {
    private Long id;
    private String title;
    private String description;
    private String startingDate;
}

