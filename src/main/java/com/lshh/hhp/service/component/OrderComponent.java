package com.lshh.hhp.service.component;

import com.lshh.hhp.dto.OrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderComponent {
    List<OrderDto> findAll();
    Optional<OrderDto> find(long id);
    List<OrderDto> findByUserId(long userId);
    Optional<OrderDto> save(OrderDto dto);
}
