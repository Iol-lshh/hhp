package com.lshh.hhp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lshh.hhp.common.dto.Request;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestPurchaseOrderDto implements Request {
    Long userId;
    List<RequestPurchaseDto> requestPurchaseList;
}
