package com.lshh.hhp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lshh.hhp.common.dto.Request;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRequestDto implements Request {
    Long userId;
    Integer toNeed;
}
