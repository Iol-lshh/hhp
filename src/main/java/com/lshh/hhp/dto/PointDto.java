package com.lshh.hhp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lshh.hhp.common.point.PointVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointDto implements PointVo {
    @JsonProperty
    Long id;
    @JsonProperty
    Integer count;
    @JsonProperty
    Long userId;
    @JsonProperty
    Integer fromType;   // PointService.PointType
    @JsonProperty
    Long fromId;    // PaymentId or PurchaseId
}
