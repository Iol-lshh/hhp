package com.lshh.hhp.order;

import com.lshh.hhp.common.Response.Result;

import java.util.List;
import java.util.Optional;

// base biz
public interface OrderBase {

    static OrderDto toDto(Order entity){
        return new OrderDto()
                .id(entity.id())
                .state(Result.of(entity.state()))
                .userId(entity.userId());
    }
    static Order toEntity(OrderDto dto){
        return new Order()
                .id(dto.id())
                .state(dto.state().ordinal())
                .userId(dto.userId());
    }

    List<OrderDto> findByUserId(long userId);

    Optional<OrderDto> find(long orderId);
    OrderDto start(long userId);
    OrderDto success(OrderDto order);
    OrderDto fail(OrderDto order);

    OrderDto startCancel(OrderDto order);
    OrderDto finishedCancel(OrderDto order);

}
