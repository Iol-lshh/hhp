package com.lshh.hhp.domain.order.item;

import java.util.List;

public interface OrderItemRepository {
    List<OrderItem> findByOrderId(long orderId);

    List<OrderItem> saveAllAndFlush(List<OrderItem> purchaseList);

    List<OrderItem> saveAll(List<OrderItem> newOrderItems);
}
