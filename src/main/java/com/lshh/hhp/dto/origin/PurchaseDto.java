package com.lshh.hhp.dto.origin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lshh.hhp.dto.Response.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseDto {
    @JsonProperty
    Long id;
    @JsonProperty
    Integer paid;
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
