package com.lshh.hhp.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true, fluent = true)
@Getter
@Setter
public class EventCancelOrderDto {
    long orderId;
}
