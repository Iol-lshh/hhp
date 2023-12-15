package com.lshh.hhp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lshh.hhp.common.dto.Request;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestExchangeDto implements Request {
    Long userId;
    Integer toNeed;
}
