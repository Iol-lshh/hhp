package com.lshh.hhp.purchase;

import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;

import java.util.List;

// delux biz
public interface PurchaseBiz1 {
    List<ViewPurchasedProductDto> favorite(Integer count);

    List<PurchaseDto> purchase(long userId, Long orderId, List<RequestPurchaseDto> purchaseRequestList) throws Exception;

    List<PurchaseDto> cancel(long orderId) throws Exception;

    boolean isPayable(long userId, List<RequestPurchaseDto> requestList);
}
