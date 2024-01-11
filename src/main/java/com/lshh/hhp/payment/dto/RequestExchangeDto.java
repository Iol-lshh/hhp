package com.lshh.hhp.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestExchangeDto {
    Long userId;
    Integer toNeed;
}
