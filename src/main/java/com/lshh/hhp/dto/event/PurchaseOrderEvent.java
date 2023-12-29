package com.lshh.hhp.dto.event;

import com.lshh.hhp.dto.request.RequestPurchaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class PurchaseOrderEvent {
    long userId;
    long orderId;
    List<RequestPurchaseDto> purchaseRequestList;
}
