package com.lshh.hhp.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true, fluent = true)
@Getter
@Setter
public class CancelOrderEvent {
    long orderId;
}
