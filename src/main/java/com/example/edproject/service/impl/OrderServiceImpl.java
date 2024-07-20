package com.example.edproject.service.impl;
import com.example.edproject.enumClass.OrderStatus;
import com.example.edproject.enumClass.OrderType;
import com.example.edproject.mapper.OrderMapper;
import com.example.edproject.model.Order;
import com.example.edproject.model.Transaction;
import com.example.edproject.result.Result;
import com.example.edproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void createOrder(Order param) {
        Order order = new Order();
        order.setOrderUUID(UUID.randomUUID().toString());
        order.setUserId(0);
        order.setStockSymbol(param.getStockSymbol());
        order.setPrice(param.getPrice());
        order.setQuantity(param.getQuantity());
        order.setAmount(param.getPrice().multiply(new BigDecimal(param.getQuantity())));
        order.setType(param.getType());
        order.setStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());
        order.setUpdateDate(LocalDateTime.now());

        orderMapper.createOrder(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return null;
    }

    @Override
    public List<Order> getOrdersByOrderTypeAndOrderStatus(OrderType type, OrderStatus status) {
        return orderMapper.getOrdersByOrderTypeAndOrderStatus(type, status);
    }


    @Override
    public List<Transaction> getTransactions() {
        return null;
    }

    @Override
    @Transactional
    public void matchOrders() {
        List<Order> buyOrders = orderMapper.getOrdersByOrderTypeAndOrderStatus(OrderType.BUY, OrderStatus.PENDING);
        List<Order> sellOrders = orderMapper.getOrdersByOrderTypeAndOrderStatus(OrderType.SELL, OrderStatus.PENDING);

        // 將買單按股票代碼分組
        Map<String, List<Order>> buyOrdersGroupedByStock = buyOrders.stream()
                .collect(Collectors.groupingBy(Order::getStockSymbol));
        //將賣單按股票代碼分組
        Map<String, List<Order>> sellOrdersGroupedByStock = sellOrders.stream()
                .collect(Collectors.groupingBy(Order::getStockSymbol));

        //獲取買單和賣單股票代碼的併集
        Set<String> allStockSymbols = new HashSet<>();
        allStockSymbols.addAll(buyOrdersGroupedByStock.keySet());
        allStockSymbols.addAll(sellOrdersGroupedByStock.keySet());

        //遍歷每個股票代碼進行訂單匹配
        for (String stockSymbol : allStockSymbols) {
            List<Order> buyOrdersForStock = buyOrdersGroupedByStock.getOrDefault(stockSymbol, Collections.emptyList());
            List<Order> sellOrdersForStock = sellOrdersGroupedByStock.getOrDefault(stockSymbol, Collections.emptyList());


            // 將買單按價格從高到低排序，價格相同按訂單ID排序
            Collections.sort(buyOrders, Comparator.comparing(Order::getPrice).reversed().thenComparing(Order::getOrderId));
            // 將賣單按價格從低到高排序，價格相同按訂單ID排序
            Collections.sort(sellOrders, Comparator.comparing(Order::getPrice).thenComparing(Order::getOrderId));

            // 創建買單和賣單的迭代器
            ListIterator<Order> buyIterator = buyOrders.listIterator();
            ListIterator<Order> sellIterator = sellOrders.listIterator();

            // 遍歷買單和賣單
            while (buyIterator.hasNext() && sellIterator.hasNext()) {
                Order buyOrder = buyIterator.next();
                Order sellOrder = sellIterator.next();

                // 如果買單價格大於等於賣單價格，進行交易
                if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) >= 0) {
                    // 計算交易數量（取買單剩餘數量和賣單數量的最小值）
                    Integer tradedQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());

                    // 更新買單和賣單的剩餘數量
                    buyOrder.setQuantity(buyOrder.getQuantity() - tradedQuantity);
                    sellOrder.setQuantity(sellOrder.getQuantity() - tradedQuantity);

                    // 如果買單數量為0，將其狀態設置為已完成並從列表中移除
                    if (buyOrder.getQuantity() == 0) {
                        buyOrder.setStatus(OrderStatus.COMPLETED);
                        buyIterator.remove();
                    }

                    // 如果賣單數量為0，將其狀態設置為已完成並從列表中移除
                    if (sellOrder.getQuantity() == 0) {
                        sellOrder.setStatus(OrderStatus.COMPLETED);
                        sellIterator.remove();
                    }

                    //更新order
                    orderMapper.updateOrder(buyOrder);//更新買單
                    orderMapper.updateOrder(sellOrder);//更新賣單
                    //新增transaction
                    Transaction transaction = new Transaction();
                    transaction.setTransactionUUID(Year.now().toString() + Double.toString(Math.random() * 1000000));
                    transaction.setBuyOrderId(buyOrder.getOrderId());
                    transaction.setSellOrderId(sellOrder.getOrderId());
                    transaction.setStockSymbol(sellOrder.getStockSymbol());
                    transaction.setPrice(sellOrder.getPrice());
                    transaction.setQuantity(tradedQuantity);
                    transaction.setTimestamp(LocalDateTime.now());

                    orderMapper.createTransaction(transaction);


                    // 記錄交易
//                recordTransaction(buyOrder, sellOrder, tradedQuantity);
                } else {
                    // 如果買單價格小於賣單價格，將賣單迭代器回退一步，保持賣單在下一次迭代中可用
                    sellIterator.previous();
                }
            }
        }


    }
}
