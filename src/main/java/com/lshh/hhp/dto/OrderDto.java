package com.lshh.hhp.dto;

import com.lshh.hhp.common.dto.Response.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class OrderDto {
    Long id;
    Long userId;
    Result state;
}
