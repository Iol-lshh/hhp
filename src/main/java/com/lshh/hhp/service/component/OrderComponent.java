package com.lshh.hhp.service.component;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.OrderDto;
import com.lshh.hhp.orm.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderComponent {
    List<OrderDto> findAll();
    Optional<OrderDto> find(long id);
    List<OrderDto> findByUserId(long userId);
    OrderDto save(OrderDto dto);

    static OrderDto toDto(Order entity){
        return new OrderDto()
                .id(entity.id())
                .state(Response.Result.of(entity.state()))
                .userId(entity.userId());
    }
    static Order toEntity(OrderDto dto){
        return new Order()
                .id(dto.id())
                .state(dto.state().ordinal())
                .userId(dto.userId());
    }

    Optional<OrderDto> findById(long id);
}
