package com.lshh.hhp.order;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.common.Response.Result;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Biz
public class OrderBizImpl implements OrderBiz {
    final OrderRepository orderRepository;

    @Override
    public OrderDto start(long userId) {
        return  OrderBiz.toDto(orderRepository.save(
            new Order()
                .userId(userId)
                .state(Result.START.ordinal())
        ));
    }

    @Override
    public OrderDto success(OrderDto order) {
        Order _order = OrderBiz.toEntity(order)
            .state(Result.SUCCESS.ordinal());
        return OrderBiz.toDto(orderRepository.save(_order));
    }

    @Override
    public OrderDto fail(OrderDto order) {
        Order _order = OrderBiz.toEntity(order)
            .state(Result.FAIL.ordinal());
        return OrderBiz.toDto(orderRepository.save(_order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findByUserId(long userId) {
        return orderRepository
            .findByUserId(userId)
            .stream()
            .map(OrderBiz::toDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDto> find(long orderId) {
        return orderRepository.findById(orderId).map(OrderBiz::toDto);
    }

    @Override
    public OrderDto startCancel(OrderDto order) {
        Order _order = OrderBiz.toEntity(order)
                .state(Result.CANCELING.ordinal());
        return OrderBiz.toDto(orderRepository.save(_order));
    }

    @Override
    public OrderDto finishedCancel(OrderDto order) {
        Order _order = OrderBiz.toEntity(order)
                .state(Result.CANCELED.ordinal());
        return OrderBiz.toDto(orderRepository.save(_order));
    }
}
