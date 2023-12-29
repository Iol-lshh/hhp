package com.lshh.hhp.observer;

import com.lshh.hhp.biz.biz1.PurchaseBiz1;
import com.lshh.hhp.dto.Response;
import com.lshh.hhp.dto.event.CancelPurchasedEvent;
import com.lshh.hhp.dto.event.CancelPurchasedOrderEvent;
import com.lshh.hhp.dto.event.PurchaseOrderEvent;
import com.lshh.hhp.dto.event.PurchaseOrderResolved;
import com.lshh.hhp.dto.origin.PurchaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PurchaseObserver {

    final PurchaseBiz1 purchaseService;
    final ApplicationEventPublisher publisher;

    @EventListener
    public void onOrderPurchase(PurchaseOrderEvent event) throws Exception {
        try{
            purchaseService.purchase(event.userId(), event.orderId(), event.purchaseRequestList());
        }catch (Exception err){
            publisher.publishEvent(
                    new PurchaseOrderResolved()
                            .orderId(event.orderId())
                            .purchaseState(Response.Result.FAIL)
            );
        }

        publisher.publishEvent(
                new PurchaseOrderResolved()
                        .orderId(event.orderId())
            .purchaseState(Response.Result.SUCCESS)
        );
    }

    @EventListener
    public void onCancelOrderPurchase(CancelPurchasedOrderEvent event) throws Exception {

        List<PurchaseDto> result = purchaseService.cancel(event.orderId());

        publisher.publishEvent(
                new CancelPurchasedEvent()
                        .orderId(event.orderId())
                        .purchasedList(result)
        );
    }
}
