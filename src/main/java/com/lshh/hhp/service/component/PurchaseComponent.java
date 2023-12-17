package com.lshh.hhp.service.component;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PurchaseDto;
import com.lshh.hhp.dto.RequestPurchaseDto;
import com.lshh.hhp.dto.ViewPurchasedProductDto;

import java.util.List;
import java.util.Optional;

public interface PurchaseComponent {
    ResultDto<List<PurchaseDto>> purchase(long userId, long orderId, List<RequestPurchaseDto> requestList) throws Exception;

    Optional<PurchaseDto> find(long id);
    List<PurchaseDto> findAll();

    boolean isPayable(long userId, List<RequestPurchaseDto> requestList);

    List<ViewPurchasedProductDto> favorite(Integer count);
}
