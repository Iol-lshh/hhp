package com.lshh.hhp.dto.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewPurchaseDto {

    Long userId;
    Long orderId;
    Long productId;
    Long purchaseId;
    String productName;
    Integer productPrice;
    Integer purchasePaid;
}
