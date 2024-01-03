package com.lshh.hhp.purchase;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.product.ProductDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.point.PointBase;
import com.lshh.hhp.product.ProductBase;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Biz(level = 1)
public class PurchaseServiceImpl implements PurchaseService {

    final PointBase pointComponent;
    final PurchaseBase purchaseComponent;
    final ProductBase productComponent;

    @Override
    @Cacheable
    public List<ViewPurchasedProductDto> favorite(Integer count) {
        return purchaseComponent.favorite(count);
    }



    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(readOnly = true)
    public boolean isPayable(long userId, List<RequestPurchaseDto> requestList){
        return pointComponent.remain(userId) >= requestList
                .stream()
                .mapToInt(request->request.getCount() * productComponent.findPrice(request.getProductId()))
                .sum();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
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
                    .count(request.getCount())
                    .state(Result.SUCCESS.ordinal()))
                .toList();

        List<PurchaseDto> purchaseDtoList = purchaseComponent.save(purchaseList);

        pointComponent.purchase(purchaseDtoList);

        return purchaseDtoList;
    }

    @Override
    @Lock(LockModeType.OPTIMISTIC)
    public List<PurchaseDto> cancel(long orderId) throws Exception {
        List<PurchaseDto> purchaseDtoList = purchaseComponent.cancledByOrderId(orderId);

        pointComponent.cancelPurchase(purchaseDtoList);
        return purchaseDtoList;
    }
}
