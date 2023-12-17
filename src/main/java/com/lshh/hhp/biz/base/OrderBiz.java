package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.Response.Result;
import com.lshh.hhp.dto.origin.OrderDto;
import com.lshh.hhp.orm.entity.Order;

import java.util.List;

public interface OrderBiz {

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

    OrderDto start(long userId);
    OrderDto success(OrderDto order);
    OrderDto fail(OrderDto order);

    List<OrderDto> findByUserId(long userId);
}
