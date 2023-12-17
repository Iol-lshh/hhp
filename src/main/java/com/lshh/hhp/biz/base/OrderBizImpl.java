package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.Response.Result;
import com.lshh.hhp.dto.origin.OrderDto;
import com.lshh.hhp.orm.entity.Order;
import com.lshh.hhp.orm.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class OrderBizImpl implements OrderBiz {
    final OrderRepository orderRepository;

    @Override
    public OrderDto start(long userId) {
        return  OrderBiz.toDto(orderRepository.save(
            new Order()
                .userId(userId)
                .state(Result.Start.ordinal())
        ));
    }

    @Override
    public OrderDto success(OrderDto order) {
        Order _order = OrderBiz.toEntity(order)
            .state(Result.OK.ordinal());
        return OrderBiz.toDto(orderRepository.save(_order));
    }

    @Override
    public OrderDto fail(OrderDto order) {
        Order _order = OrderBiz.toEntity(order)
            .state(Result.FAIL.ordinal());
        return OrderBiz.toDto(orderRepository.save(_order));
    }

    @Override
    public List<OrderDto> findByUserId(long userId) {
        return orderRepository
            .findByUserId(userId)
            .stream()
            .map(OrderBiz::toDto)
            .toList();
    }
}
