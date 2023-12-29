package com.lshh.hhp.dto.event;

import com.lshh.hhp.purchase.PurchaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class CancelPurchasedEvent {
    long orderId;
    List<PurchaseDto> purchasedList;
}
