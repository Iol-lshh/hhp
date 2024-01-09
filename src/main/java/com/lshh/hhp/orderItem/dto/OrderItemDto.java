<<<<<<<< HEAD:src/main/java/com/lshh/hhp/dto/origin/PurchaseDto.java
package com.lshh.hhp.dto.origin;
========
package com.lshh.hhp.orderItem.dto;
>>>>>>>> origin/ch3st1_aws:src/main/java/com/lshh/hhp/orderItem/dto/OrderItemDto.java

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
