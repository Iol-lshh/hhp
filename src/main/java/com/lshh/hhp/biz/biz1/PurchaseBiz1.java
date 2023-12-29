package com.lshh.hhp.biz.biz1;

import com.lshh.hhp.dto.origin.PurchaseDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;

import java.util.List;

public interface PurchaseBiz1 {
    List<ViewPurchasedProductDto> favorite(Integer count);

    List<PurchaseDto> purchase(long userId, Long orderId, List<RequestPurchaseDto> purchaseRequestList) throws Exception;

    List<PurchaseDto> cancel(long orderId) throws Exception;

    boolean isPayable(long userId, List<RequestPurchaseDto> requestList);
}
