package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.origin.PurchaseDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.orm.entity.Purchase;

import java.util.List;

public interface PurchaseBiz {

    static PurchaseDto toDto(Purchase entity){
        return new PurchaseDto()
                .id(entity.id())
                .paid(entity.paid())
                .count(entity.count())
                .productId(entity.productId())
                .userId(entity.userId())
                .orderId(entity.orderId());
    }
    static Purchase toEntity(PurchaseDto dto){
        return new Purchase()
                .id(dto.id())
                .paid(dto.paid())
                .count(dto.count())
                .productId(dto.productId())
                .userId(dto.userId())
                .orderId(dto.orderId());
    }

    List<ViewPurchasedProductDto> favorite(Integer count);

    List<PurchaseDto> save(List<Purchase> purchaseList);
}
