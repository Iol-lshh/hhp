package com.lshh.hhp.order.repository;

import com.lshh.hhp.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order, Long> {
   List<Order> findByUserId(long userId);
}
