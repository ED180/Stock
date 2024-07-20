package com.example.edproject.controller;
import com.example.edproject.enumClass.OrderStatus;
import com.example.edproject.model.Order;
import com.example.edproject.model.Transaction;
import com.example.edproject.result.Result;
import com.example.edproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/order/create")
    public Result createOrder(@RequestBody Order order) {
        orderService.createOrder(order);
        return Result.genSuccessResult();
    }

    @GetMapping("/order/{orderId}")
    public Result getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return Result.genSuccessResult(order);
    }

    @GetMapping("/transaction")
    public Result<List<Transaction>> getTransactions() {
        List<Transaction> transactions = orderService.getTransactions();
        return Result.genSuccessResult(transactions);
    }

    @PostMapping("/order/match")
    public Result matchOrders() {
        orderService.matchOrders();
        return Result.genSuccessResult();
    }

}
