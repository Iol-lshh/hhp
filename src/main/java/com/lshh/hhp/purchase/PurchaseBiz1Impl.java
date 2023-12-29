package com.lshh.hhp.purchase;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.product.ProductDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.point.PointBiz;
import com.lshh.hhp.product.ProductBiz;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Biz(level = 1)
public class PurchaseBiz1Impl implements PurchaseBiz1 {

    final PointBiz pointComponent;
    final PurchaseBiz purchaseComponent;
    final ProductBiz productComponent;

    @Override
    @Transactional(readOnly = true)
    public List<ViewPurchasedProductDto> favorite(Integer count) {
        return purchaseComponent.favorite(count);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPayable(long userId, List<RequestPurchaseDto> requestList){
        return pointComponent.remain(userId) >= requestList
                .stream()
                .mapToInt(request->request.getCount() * productComponent.findPrice(request.getProductId()))
                .sum();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<PurchaseDto> purchase(long userId, Long orderId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {

        //  ## 아이디 포인트 확인
        if (!isPayable(userId, purchaseRequestList)) {
            throw new Exception("포인트 부족");
        }

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

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<PurchaseDto> cancel(long orderId) throws Exception {
        List<PurchaseDto> purchaseDtoList = purchaseComponent.cancledByOrderId(orderId);

        pointComponent.cancel(purchaseDtoList);
        return purchaseDtoList;
    }
}
