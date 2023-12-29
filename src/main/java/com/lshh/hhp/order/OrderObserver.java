package com.lshh.hhp.order;

import com.lshh.hhp.common.Observer;
import com.lshh.hhp.dto.event.PurchaseOrderResolved;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Observer
public class OrderObserver {
    final OrderOrchestrator orderService;

    @EventListener
    public void resolveSuccessOrderPurchase(PurchaseOrderResolved event){
        switch (event.orderState()){
            case SUCCESS -> orderService.success(event.orderId());
            case FAIL -> orderService.fail(event.orderId());
            case CANCELING -> orderService.startCancel(event.orderId());
            case CANCELED -> orderService.finishedCancel(event.orderId());
        }
    }
}
