package com.example.edproject.model;

import com.example.edproject.enumClass.OrderStatus;
import com.example.edproject.enumClass.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Order {
    private Long OrderId;

    private String orderUUID;

    private Integer userId;

    private String stockSymbol;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal amount;

    //BUY orSELL
    private OrderType type;

    //PENDING, COMPLETED, CANCELLED
    private OrderStatus status;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

}


