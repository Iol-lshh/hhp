package com.lshh.hhp.domain.order;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.common.annotation.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OrderService {
    final OrderRepository orderRepository;

    @Transactional
    public Order start(long userId) {
        return  orderRepository.save(
                Order.createNewOrder(userId)
        );
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> findFailedByUserId(Long userId) {
        return orderRepository.findByUserIdAndState(userId, Response.Result.FAIL.ordinal());
    }

    @Transactional(readOnly = true)
    public List<Order> findByUserId(long userId) {
        return orderRepository
                .findByUserId(userId)
                .stream()
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Order> find(long orderId) {
        return orderRepository.findById(orderId);
    }

}