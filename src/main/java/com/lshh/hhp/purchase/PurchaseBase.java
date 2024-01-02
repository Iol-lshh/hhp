package com.lshh.hhp.purchase;

import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;

import java.util.List;

// base biz
public interface PurchaseBase {

    static PurchaseDto toDto(Purchase entity){
        return new PurchaseDto()
                .id(entity.id())
                .paid(entity.paid())
                .count(entity.count())
                .productId(entity.productId())
                .userId(entity.userId())
                .orderId(entity.orderId())
                .state(Result.of(entity.state()));
    }
    static Purchase toEntity(PurchaseDto dto){
        return new Purchase()
                .id(dto.id())
                .paid(dto.paid())
                .count(dto.count())
                .productId(dto.productId())
                .userId(dto.userId())
                .orderId(dto.orderId())
                .state(dto.state().ordinal());
    }

    List<ViewPurchasedProductDto> favorite(Integer count);

    List<PurchaseDto> save(List<Purchase> purchaseList);

    List<PurchaseDto> cancledByOrderId(long orderId);
}
