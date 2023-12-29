package com.lshh.hhp.dto.event;

import com.lshh.hhp.dto.Response.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class PurchaseOrderResolved {

    long orderId;

    Result orderState;  // 전체 트랜잭션
    Result purchaseState;   // 구매 트랜잭션
    Result stockState;  // 재고 트랜잭션
}
