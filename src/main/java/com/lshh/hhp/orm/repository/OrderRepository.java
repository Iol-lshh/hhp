package com.lshh.hhp.orm.repository;

import com.lshh.hhp.orm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order, Long> {
   List<Order> findByUserId(long userId);
}
