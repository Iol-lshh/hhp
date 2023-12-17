package com.lshh.hhp.service.component;

import com.lshh.hhp.dto.OrderDto;
import com.lshh.hhp.orm.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderComponentDefault implements OrderComponent {

    final OrderRepository orderRepository;

    @Override
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream().map(OrderComponent::toDto).toList();
    }

    @Override
    public Optional<OrderDto> find(long id) {
        return orderRepository.findById(id).map(OrderComponent::toDto);
    }

    @Override
    public List<OrderDto> findByUserId(long userId) {
        return orderRepository.findByUserId(userId).stream().map(OrderComponent::toDto).toList();
    }

    @Override
    public OrderDto save(OrderDto dto) {
        return OrderComponent.toDto(orderRepository.save(OrderComponent.toEntity(dto)));
    }

    @Override
    public Optional<OrderDto> findById(long id) {
        return orderRepository.findById(id).map(OrderComponent::toDto);
    }
}
