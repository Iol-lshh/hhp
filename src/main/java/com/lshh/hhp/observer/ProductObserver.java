package com.lshh.hhp.observer;

import com.lshh.hhp.biz.base.ProductBiz;
import com.lshh.hhp.dto.event.OrderPurchaseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductObserver {
    final ProductBiz productService;

    @EventListener
    public void onOrderPurchase(OrderPurchaseEvent event) throws Exception {
        productService.unstore(event.purchaseRequestList());
    }
}
