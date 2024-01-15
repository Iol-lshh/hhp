package com.lshh.hhp.order.service;

import com.lshh.hhp.order.Order;

import java.util.List;
import java.util.Optional;

/**
 * level 0
 * The OrderService interface defines methods to manage orders.
 */
public interface OrderService {
    List<Order> findByUserId(long userId);
    Optional<Order> find(long orderId);
    Order start(long userId);
    Order save(Order order);
}
