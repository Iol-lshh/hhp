package com.lshh.hhp.product.service;

import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.product.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * level 0
 * ProductService interface provides methods to interact with products and perform various operations.
 */
public interface ProductService {
    boolean validate(List<RequestPurchaseDto> purchaseRequestList);

    boolean isInStock(List<RequestPurchaseDto> purchaseRequestList);

    int findPrice(Long productId);

    List<Product> deduct(List<RequestPurchaseDto> purchaseList) throws Exception;

    List<Product> conduct(List<OrderItemDto> purchaseList) throws Exception;

    List<Product> findAll();

    Optional<Product> find(Long productId);

    List<OrderItem> putPrice(List<OrderItem> dtos);

    int findPrice(long productId);
}
