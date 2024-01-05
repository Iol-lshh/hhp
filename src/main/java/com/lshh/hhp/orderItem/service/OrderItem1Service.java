package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.product.dto.ProductDto;

import java.util.List;

/**
 * level 1
 * The OrderItem1Service interface provides methods to handle order item-related operations.
 */
public interface OrderItem1Service {
    List<ProductDto> favorite(Integer count);

    List<OrderItemDto> orderEachProduct(long userId, long orderId, List<RequestPurchaseDto> purchaseRequestList) throws Exception;

    List<OrderItemDto> cancel(long orderId) throws Exception;
}
