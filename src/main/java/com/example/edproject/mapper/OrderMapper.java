package com.example.edproject.mapper;
import com.example.edproject.enumClass.OrderStatus;
import com.example.edproject.enumClass.OrderType;
import com.example.edproject.model.Order;
import com.example.edproject.model.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO orders (orderId, orderUUID, userId, stockSymbol, price, quantity, amount, type, status, createDate, updateDate) " +
            "VALUES (#{order.orderId}, #{order.orderUUID}, #{order.userId}, #{order.stockSymbol}, #{order.price}, #{order.quantity}, #{order.amount}, #{order.type}, #{order.status}, #{order.createDate}, #{order.updateDate})")
    void createOrder(@Param("order") Order order);

    @Update("UPDATE orders SET quantity = #{order.quantity}, status = #{order.status} WHERE orderId = #{order.orderId}")
    void updateOrder(@Param("order") Order order);

    @Insert("INSERT INTO transactions (transactionId, transactionUUID, buyOrderId, sellOrderId, stockSymbol, price, quantity, timestamp) " +
            "VALUES (#{transaction.transactionId}, #{transaction.transactionUUID}, #{transaction.buyOrderId}, #{transaction.sellOrderId}, #{transaction.stockSymbol}, #{transaction.price}, #{transaction.quantity}, #{transaction.timestamp})")
    void createTransaction(@Param("transaction") Transaction transaction);
    @Select("SELECT * FROM orders WHERE type = #{type} AND status = #{status}")
    List<Order> getOrdersByOrderTypeAndOrderStatus(@Param("type") OrderType type, @Param("status") OrderStatus status);
    void getTransactions(Transaction transaction);


}
