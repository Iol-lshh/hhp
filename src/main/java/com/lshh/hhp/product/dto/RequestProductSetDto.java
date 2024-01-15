package com.lshh.hhp.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestProductSetDto {
    Long userId;
    List<RequestProductDto> productDtoList;
}
