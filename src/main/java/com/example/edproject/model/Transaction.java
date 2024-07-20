package com.example.edproject.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction {
    private Long transactionId;
    private String transactionUUID;
    //買單的orderID
    private Long buyOrderId;
    //賣單的orderID
    private Long sellOrderId;
    private String stockSymbol;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime timestamp;
}
