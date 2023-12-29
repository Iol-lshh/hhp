package com.lshh.hhp.observer;

import com.lshh.hhp.biz.biz2.OrderBiz2;
import com.lshh.hhp.dto.event.OrderPurchaseResolved;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderObserver {
    final OrderBiz2 orderService;

    @EventListener
    public void resolveSuccessOrderPurchase(OrderPurchaseResolved event){
        switch (event.orderState()){
            case SUCCESS -> orderService.success(event.orderId());
            case FAIL -> orderService.fail(event.orderId());
            case CANCELING -> orderService.startCancel(event.orderId());
            case CANCELED -> orderService.finishedCancel(event.orderId());
        }
    }
}
