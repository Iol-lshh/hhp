package com.lshh.hhp.dto.view;

import com.lshh.hhp.dto.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ViewPurchasedProductDto implements Response {
    Long id;
    String name;
    Integer price;
    Integer paidCnt;
    Integer orderByPaidCnt;
}
