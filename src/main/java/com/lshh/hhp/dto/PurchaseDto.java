package com.lshh.hhp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class PurchaseDto {
    Long id;
    String name;
    Integer paid;

    Long productId;
    Long userId;
}
