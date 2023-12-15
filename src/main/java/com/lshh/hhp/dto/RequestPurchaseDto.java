package com.lshh.hhp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lshh.hhp.common.dto.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestPurchaseDto implements Request {
    Long productId;
    Integer count;
}
