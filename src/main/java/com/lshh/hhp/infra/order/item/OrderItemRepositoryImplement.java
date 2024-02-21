package com.lshh.hhp.infra.order.item;

import com.lshh.hhp.domain.order.item.OrderItemRepository;
import com.lshh.hhp.domain.order.item.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImplement implements OrderItemRepository {
    final OrderItemJpaRepository orderItemJpaRepository;

    @Override
    public List<OrderItem> findByOrderId(long orderId) {
        return orderItemJpaRepository.findByOrderId(orderId);
    }

    @Override
    public List<OrderItem> saveAllAndFlush(List<OrderItem> purchaseList) {
        return orderItemJpaRepository.saveAllAndFlush(purchaseList);
    }

    @Override
    public List<OrderItem> saveAll(List<OrderItem> newOrderItems) {
        return orderItemJpaRepository.saveAll(newOrderItems);
    }
}
