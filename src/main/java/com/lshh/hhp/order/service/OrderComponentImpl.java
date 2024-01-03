package com.lshh.hhp.order.service;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.order.Order;
import com.lshh.hhp.order.dto.OrderDto;
import com.lshh.hhp.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Biz
public class OrderComponentImpl implements OrderComponent {
    final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order start(long userId) {
        return  orderRepository.save(
            Order.createNewOrder(userId)
        );
    }

    @Override
    @Transactional
    public void setState(long orderId, Result state) throws Exception{
        Order order = orderRepository.findById(orderId)
                .orElseThrow(Exception::new);
        order.setState(state);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByUserId(long userId) {
        return orderRepository
            .findByUserId(userId)
            .stream()
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> find(long orderId) {
        return orderRepository.findById(orderId);
    }

}
