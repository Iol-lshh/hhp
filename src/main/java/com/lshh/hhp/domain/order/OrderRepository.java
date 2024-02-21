package com.lshh.hhp.domain.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
   List<Order> findByUserId(long userId);

   List<Order> findByUserIdAndState(long userId, int state);

   Order save(Order newOrder);

   Optional<Order> findById(long orderId);
}
