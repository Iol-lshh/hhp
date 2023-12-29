package com.lshh.hhp.observer;

import com.lshh.hhp.biz.biz1.PurchaseBiz1;
import com.lshh.hhp.dto.event.OrderPurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PurchaseObserver {

    final PurchaseBiz1 purchaseService;

    @EventListener
    public void onOrderPurchase(OrderPurchaseEvent event) throws Exception {
        purchaseService.purchase(event.userId(), event.orderId(), event.purchaseRequestList());
    }
}
