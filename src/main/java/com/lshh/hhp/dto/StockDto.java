package com.lshh.hhp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class StockDto {

    Long id;
    String name;

    Long productId;
    Long purchaseId;
}
