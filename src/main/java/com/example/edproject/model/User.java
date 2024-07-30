package com.example.edproject.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class User {
    private Long userId;
    private String email;
    private String password;
    private LocalDateTime registeredDate;
    private List<Asset> allAsset;
    private BigDecimal totalAmount;
}
