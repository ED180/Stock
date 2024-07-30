package com.example.edproject.dto;

import lombok.Data;

@Data
public class Register {
    private String email;
    private String password;
    private Integer totalAmount = 0;
}
