package com.lshh.hhp.biz.biz1;

import com.lshh.hhp.dto.origin.ProductDto;
import com.lshh.hhp.dto.origin.PurchaseDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.orm.entity.Purchase;
import com.lshh.hhp.biz.base.PointBiz;
import com.lshh.hhp.biz.base.ProductBiz;
import com.lshh.hhp.biz.base.PurchaseBiz;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class PurchaseBiz1Impl implements PurchaseBiz1 {

    final PointBiz pointComponent;
    final PurchaseBiz purchaseComponent;
    final ProductBiz productComponent;

    @Override
    public List<ViewPurchasedProductDto> favorite(Integer count) {
        return purchaseComponent.favorite(count);
    }

    @Override
    public List<PurchaseDto> purchase(long userId, Long orderId, List<RequestPurchaseDto> purchaseRequestList) {
        List<Purchase> purchaseList = purchaseRequestList
                .stream()
                .map(request -> new Purchase()
                    .userId(userId)
                    .orderId(orderId)
                    .productId(request.getProductId())
                    .paid(request.getCount()
                        * productComponent.find(request.getProductId())
                            .map(ProductDto::price)
                            .orElse(0))
                    .count(request.getCount()))
                .toList();

        List<PurchaseDto> purchaseDtoList = purchaseComponent.save(purchaseList);

        pointComponent.purchase(purchaseDtoList);

        return purchaseDtoList;
    }
}
