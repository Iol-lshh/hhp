package com.lshh.hhp.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestPurchaseOrderDto {
    Long userId;
    List<RequestPurchaseDto> requestPurchaseList;
}
