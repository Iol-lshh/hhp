package com.lshh.hhp.domain.point.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointDto {
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
