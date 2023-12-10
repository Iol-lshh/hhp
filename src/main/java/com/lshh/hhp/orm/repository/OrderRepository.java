package com.lshh.hhp.orm.repository;

import com.lshh.hhp.orm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Order, Long> {
}
