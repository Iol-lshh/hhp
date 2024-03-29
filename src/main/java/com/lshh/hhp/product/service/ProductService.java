package com.lshh.hhp.product.service;

import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.order.dto.RequestPurchaseDto;
import com.lshh.hhp.product.Product;

import java.util.List;
import java.util.Optional;

/**
 * level 0
 * ProductService interface provides methods to interact with products and perform various operations.
 */
public interface ProductService {
    boolean validate(List<RequestPurchaseDto> purchaseRequestList);

    boolean isInStock(List<OrderItem> orderItems);

    int findPrice(Long productId);

    List<Product> deduct(List<OrderItem> orderItems) throws Exception;

    List<Product> conduct(List<OrderItem> purchaseList) throws Exception;

    List<Product> findAll();

    Optional<Product> find(Long productId);

    List<OrderItem> putPrice(List<OrderItem> dtos);

    int findPrice(long productId);

    //    @Transactional(readOnly = true)
    List<Product> favorite(Integer count);
}
