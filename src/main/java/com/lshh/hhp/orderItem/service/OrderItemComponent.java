package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.orderItem.dto.OrderItemDto;

import java.util.List;

// base biz
public interface OrderItemComponent {

    // todo 컨버팅 함수들 엔티티로 이동
    static OrderItemDto toDto(OrderItem entity){
        return new OrderItemDto()
                .id(entity.id())
                .paid(entity.paid())
                .count(entity.count())
                .productId(entity.productId())
                .userId(entity.userId())
                .orderId(entity.orderId())
                .state(Result.of(entity.state()));
    }
    static OrderItem toEntity(OrderItemDto dto){
        return new OrderItem()
                .id(dto.id())
                .paid(dto.paid())
                .count(dto.count())
                .productId(dto.productId())
                .userId(dto.userId())
                .orderId(dto.orderId())
                .state(dto.state().ordinal());
    }

    List<ViewPurchasedProductDto> favorite(Integer count);

    List<OrderItemDto> save(List<OrderItem> purchaseList);

    List<OrderItemDto> cancledByOrderId(long orderId);
}
