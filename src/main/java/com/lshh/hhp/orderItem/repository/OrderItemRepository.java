package com.lshh.hhp.orderItem.repository;

import com.lshh.hhp.orderItem.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(long orderId);
}
