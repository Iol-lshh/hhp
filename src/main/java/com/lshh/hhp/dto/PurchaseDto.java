package com.lshh.hhp.dto;

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
public class PurchaseDto {
    @JsonProperty
    Long id;
    @JsonProperty
    Integer paid;
    @JsonProperty
    Long productId;
    @JsonProperty
    Long userId;
}
