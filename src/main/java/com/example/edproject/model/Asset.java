package com.example.edproject.model;

import lombok.Data;

import java.math.BigDecimal;
import java.net.Inet4Address;

@Data
public class Asset {
    private String stockSymbol;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amount;
}
