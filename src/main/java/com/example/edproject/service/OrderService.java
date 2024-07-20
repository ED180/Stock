package com.example.edproject.service;
import com.example.edproject.enumClass.OrderStatus;
import com.example.edproject.enumClass.OrderType;
import com.example.edproject.model.Order;
import com.example.edproject.model.Transaction;
import com.example.edproject.result.Result;

import java.util.List;

public interface OrderService {
    void createOrder(Order order);
    Order getOrderById(Long orderId);

    List<Order> getOrdersByOrderTypeAndOrderStatus(OrderType type, OrderStatus status);
    List<Transaction> getTransactions();

    void matchOrders();
//    Result sellRequest(SellRequest sellRequest);
//    Result buyRequest(BuyRequest buyRequest);
}
