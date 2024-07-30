package com.example.edproject.cache;

import ch.qos.logback.core.util.TimeUtil;
import com.example.edproject.config.RedisConfig;
import com.example.edproject.enumClass.OrderStatus;
import com.example.edproject.enumClass.OrderType;
import com.example.edproject.model.Order;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.util.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class OrderCache {
    private static final String ORDER_PREFIX = "order:";
    private static final String ORDER_INDEX_PREFIX = "orderIndex:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

//    ValueOperations<String, Object> operations = redisTemplate.opsForValue();

    public OrderCache() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void saveOrder(Order order) {
        String orderKey = ORDER_PREFIX + order.getOrderUUID();
        redisTemplate.opsForValue().set(orderKey, order);

        String indexKey = ORDER_INDEX_PREFIX + order.getType() + "_" + order.getStatus();
        redisTemplate.opsForSet().add(indexKey, order.getOrderUUID());
    }

    public Order getOrder(String key) {
        Object object = redisTemplate.opsForValue().get(ORDER_PREFIX + key);
        if (object instanceof Map) {
            return objectMapper.convertValue(object, Order.class);
        }
        return (Order) object;
    }


    public List<Order> getOrdersByTypeAndStatus(OrderType type, OrderStatus status) {
        String indexKey = ORDER_INDEX_PREFIX + type + "_" + status;
        System.out.println("Generated indexKey: " + indexKey); // 调试信息

        Set<Object> orderUUIDs = redisTemplate.opsForSet().members(indexKey);
        System.out.println("Retrieved orderUUIDs: " + orderUUIDs); // 调试信息

        List<Order> orders = new ArrayList<>();
        System.out.println("這邊");
        if (orderUUIDs != null) {
            System.out.println("1");
            System.out.println("1之後:" + orderUUIDs);
            for (Object uuid : orderUUIDs) {
                System.out.println("2");
                System.out.println("2之後:" + uuid);
                if (uuid instanceof String) {
                    System.out.println("3");
                    System.out.println("3之後:" + uuid);
                    try {
                        System.out.println("4");
                        Order order = getOrder((String) uuid);
                        System.out.println("4之後的order:" + order);
                        System.out.println("5");
                        if (order != null) {
                            orders.add(order);
                            System.out.println("6");
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to get order for UUID: " + uuid);
                        e.printStackTrace(); // 捕捉并打印异常
                    }
                } else {
                    System.out.println("Unexpected type for UUID: " + uuid.getClass()); // 调试信息
                }
            }
        }
        System.out.println("全部都沒有");
        return orders;

    }

    public void updateOrder(Order updatedOrder) {
        String orderKey = ORDER_PREFIX + updatedOrder.getOrderUUID();

        Order existingOrder = getOrder(updatedOrder.getOrderUUID());
        if (existingOrder == null) {
            throw new IllegalArgumentException("Order not found in cache");
        }

        if (updatedOrder.getStatus().equals(OrderStatus.COMPLETED)) {
            redisTemplate.delete(orderKey);

            String oldIndexKey = ORDER_INDEX_PREFIX + existingOrder.getType() + "_" + existingOrder.getStatus();
            redisTemplate.opsForSet().remove(oldIndexKey, existingOrder.getOrderUUID());
        } else {
            if (!existingOrder.getType().equals(updatedOrder.getType()) ||
                    !existingOrder.getStatus().equals(updatedOrder.getStatus())) {

                String oldIndexKey = ORDER_INDEX_PREFIX + existingOrder.getType() + "_" + existingOrder.getStatus();
                redisTemplate.opsForSet().remove(oldIndexKey, existingOrder.getOrderUUID());

                String newIndexKey = ORDER_INDEX_PREFIX + updatedOrder.getType() + "_" + updatedOrder.getStatus();
                redisTemplate.opsForSet().add(newIndexKey, updatedOrder.getOrderUUID());
            }

            redisTemplate.opsForValue().set(orderKey, updatedOrder);
        }
    }

    public void login(String token) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();

        operations.set(token, token, 1, TimeUnit.HOURS);

    }

    public void logout(String token) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();

        redisTemplate.delete(token);

    }
}
