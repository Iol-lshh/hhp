package com.lshh.hhp.dto;

import com.lshh.hhp.common.dto.Response;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
public class ViewPurchasedProductDto implements Response {
    Long id;
    String name;
    Integer price;
    Integer paidCnt;
}
