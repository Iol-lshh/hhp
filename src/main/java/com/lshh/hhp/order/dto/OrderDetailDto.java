package com.lshh.hhp.order.dto;

import com.lshh.hhp.orderItem.dto.OrderItemDto;
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
