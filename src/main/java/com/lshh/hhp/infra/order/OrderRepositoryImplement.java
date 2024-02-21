package com.lshh.hhp.infra.order;

import com.lshh.hhp.domain.order.Order;
import com.lshh.hhp.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImplement implements OrderRepository {

    final OrderJpaRepository orderJpaRepository;

    @Override
    public List<Order> findByUserId(long userId) {
        return orderJpaRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findByUserIdAndState(long userId, int state) {
        return orderJpaRepository.findByUserIdAndState(userId, state);
    }

    @Override
    public Order save(Order newOrder) {
        return orderJpaRepository.save(newOrder);
    }

    @Override
    public Optional<Order> findById(long orderId) {
        return orderJpaRepository.findById(orderId);
    }
}
