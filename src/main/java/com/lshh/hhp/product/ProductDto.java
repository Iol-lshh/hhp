package com.lshh.hhp.product;

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
public class ProductDto {
    @JsonProperty
    Long id;
    @JsonProperty
    String name;
    @JsonProperty
    Integer price;
    @JsonProperty
    Integer stockCnt;
}