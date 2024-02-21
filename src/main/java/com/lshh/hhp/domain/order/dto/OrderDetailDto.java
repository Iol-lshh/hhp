package com.lshh.hhp.domain.order.dto;

import com.lshh.hhp.domain.order.item.dto.OrderItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
public class OrderDetailDto {
    OrderDto order;
    List<OrderItemDto> orderItems;
}
