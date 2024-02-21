package com.lshh.hhp.infra.order;

import com.lshh.hhp.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
   List<Order> findByUserId(long userId);

   List<Order> findByUserIdAndState(long userId, int state);
}
