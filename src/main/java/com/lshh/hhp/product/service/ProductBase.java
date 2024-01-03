package com.lshh.hhp.product.service;

import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.product.Product;
import com.lshh.hhp.product.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductBase {
    static ProductDto toDto(Product entity){
        return new ProductDto()
                .id(entity.id())
                .name(entity.name())
                .price(entity.price())
                .stockCnt(entity.stockCnt());
    }
    static Product toEntity(ProductDto dto){
        return new Product()
                .id(dto.id())
                .name(dto.name())
                .price(dto.price())
                .stockCnt(dto.stockCnt());
    }

    boolean validate(List<RequestPurchaseDto> purchaseRequestList);

    boolean isInStock(List<RequestPurchaseDto> purchaseRequestList);

    int findPrice(Long productId);

    List<ProductDto> deduct(List<RequestPurchaseDto> purchaseList) throws Exception;

    List<ProductDto> conduct(List<OrderItemDto> purchaseList) throws Exception;

    List<ProductDto> findAll();

    Optional<ProductDto> find(Long productId);

}
