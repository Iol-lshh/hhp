package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.product.dto.RequestProductDto;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.product.dto.ProductDto;

import java.util.List;

/**
 * level 1
 * The OrderItem1Service interface provides methods to handle order item-related operations.
 */
public interface OrderItem1Service {
    List<ProductDto> favorite(Integer count);

    List<OrderItem> orderEachProduct(long userId, long orderId, List<RequestProductDto> purchaseRequestList) throws Exception;

    List<OrderItem> cancelOrderItem(long orderId) throws Exception;
}
