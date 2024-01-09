package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.orderItem.OrderItem;

import java.util.List;

/**
 * level 0
 * The OrderItemService interface provides functionality related to order items.
 */
public interface OrderItemService {

    List<ViewPurchasedProductDto> favorite(Integer count);

    List<OrderItem> save(List<OrderItem> purchaseList);

    List<OrderItem> canceldByOrderId(long orderId);
}
