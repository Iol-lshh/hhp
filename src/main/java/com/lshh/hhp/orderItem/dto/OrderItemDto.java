package com.lshh.hhp.orderItem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lshh.hhp.common.Response.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto {
    @JsonProperty
    Long id;
    @JsonProperty
    Integer toPay;
    @JsonProperty
    Integer count;
    @JsonProperty
    Long productId;
    @JsonProperty
    Long userId;
    @JsonProperty
    Long orderId;
    @JsonProperty
    Result state;
}
