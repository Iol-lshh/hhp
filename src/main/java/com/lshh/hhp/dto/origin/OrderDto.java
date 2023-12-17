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
public class OrderDto {
    @JsonProperty
    Long id;
    @JsonProperty
    Long userId;
    @JsonProperty
    Result state;
}
