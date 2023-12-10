package com.lshh.hhp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class PointDto {
    Long id;
    String name;
    Integer count;

    Long userId;

    Integer fromType;   // PointService.PointType
    Long fromId;    // PaymentId or PurchaseId
}
