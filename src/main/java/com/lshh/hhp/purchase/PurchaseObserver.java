package com.lshh.hhp.purchase;

import com.lshh.hhp.purchase.PurchaseBiz1;
import com.lshh.hhp.common.Observer;
import com.lshh.hhp.common.Response;
import com.lshh.hhp.dto.event.CancelPurchasedEvent;
import com.lshh.hhp.dto.event.CancelPurchasedOrderEvent;
import com.lshh.hhp.dto.event.PurchaseOrderEvent;
import com.lshh.hhp.dto.event.PurchaseOrderResolved;
import com.lshh.hhp.purchase.PurchaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import java.util.List;

@RequiredArgsConstructor
@Observer
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
