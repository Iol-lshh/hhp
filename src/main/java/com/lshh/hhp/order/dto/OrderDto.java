<<<<<<<< HEAD:src/main/java/com/lshh/hhp/dto/origin/OrderDto.java
package com.lshh.hhp.dto.origin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lshh.hhp.dto.Response.Result;
========
package com.lshh.hhp.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lshh.hhp.common.Response.Result;
import lombok.AllArgsConstructor;
>>>>>>>> origin/ch3st1_aws:src/main/java/com/lshh/hhp/order/dto/OrderDto.java
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
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
