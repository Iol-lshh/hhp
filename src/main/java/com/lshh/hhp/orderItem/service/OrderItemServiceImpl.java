package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.product.dto.ProductDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.point.service.PointBase;
import com.lshh.hhp.product.service.ProductBase;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Biz(level = 1)
public class OrderItemServiceImpl implements OrderItemService {

    final PointBase pointComponent;
    final OrderItemComponent orderItemComponent;
    final ProductBase productComponent;

    @Override
    @Cacheable
    public List<ViewPurchasedProductDto> favorite(Integer count) {
        return orderItemComponent.favorite(count);
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
    public List<OrderItemDto> purchase(long userId, Long orderId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {

        //  ## 아이디 포인트 확인
        if (!isPayable(userId, purchaseRequestList)) {
            throw new Exception("포인트 부족");
        }

        // todo
        List<OrderItem> orderItemList = purchaseRequestList
                .stream()
                .map(request -> new OrderItem()
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

        List<OrderItemDto> orderItemDtoList = orderItemComponent.save(orderItemList);

        pointComponent.subtract(orderItemDtoList);

        return orderItemDtoList;
    }

    @Override
    @Lock(LockModeType.OPTIMISTIC)
    public List<OrderItemDto> cancel(long orderId) throws Exception {
        List<OrderItemDto> purchaseDtoList = orderItemComponent.cancledByOrderId(orderId);

        pointComponent.cancelSubtract(purchaseDtoList);
        return purchaseDtoList;
    }
}
