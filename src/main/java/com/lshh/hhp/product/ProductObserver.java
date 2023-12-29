package com.lshh.hhp.product;

import com.lshh.hhp.product.ProductBiz;
import com.lshh.hhp.common.Observer;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.dto.event.CancelPurchasedEvent;
import com.lshh.hhp.dto.event.CancelPurchasedOrderResolved;
import com.lshh.hhp.dto.event.PurchaseOrderEvent;
import com.lshh.hhp.dto.event.PurchaseOrderResolved;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Observer
public class ProductObserver {
    final ProductBiz productService;
    final ApplicationEventPublisher publisher;

    @EventListener
    public void onOrderPurchase(PurchaseOrderEvent event) {
        try{
            productService.unstore(event.purchaseRequestList());
        }catch (Exception err){
            publisher.publishEvent(
                    new PurchaseOrderResolved()
                            .orderId(event.orderId())
                            .stockState(Result.FAIL)
            );
        }

        publisher.publishEvent(
                new PurchaseOrderResolved()
                        .orderId(event.orderId())
                        .stockState(Result.SUCCESS)
        );
    }

    @EventListener
    public void onCancelOrderPurchase(CancelPurchasedEvent event) throws Exception {

        productService.restore(event.purchasedList());

        publisher.publishEvent(
            new CancelPurchasedOrderResolved()
                .orderId(event.orderId())
                .stockState(Result.CANCELED)
        );
    }
}
