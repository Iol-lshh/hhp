package com.lshh.hhp.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestPurchaseDto {
    Long productId;
    Integer count;
}
