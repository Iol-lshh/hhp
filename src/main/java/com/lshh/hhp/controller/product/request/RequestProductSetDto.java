package com.lshh.hhp.controller.product.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lshh.hhp.domain.product.dto.RequestProductDto;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestProductSetDto {
    Long userId;
    List<RequestProductDto> productDtoList;
}
