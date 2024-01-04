package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.common.Service;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.point.service.PointService;
import com.lshh.hhp.product.service.ProductService;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service(level = 1)
public class OrderItem1ServiceImpl implements OrderItem1Service {

    final PointService pointComponent;
    final OrderItemService orderItemComponent;
    final ProductService productComponent;

    @Override
    @Cacheable
    public List<ViewPurchasedProductDto> favorite(Integer count) {
        return orderItemComponent.favorite(count);
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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public List<OrderItemDto> orderEachProduct(long userId, long orderId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {

        //  ## 아이디 포인트 확인
        if (!isPayable(userId, purchaseRequestList)) {
            throw new Exception("포인트 부족");
        }
        
        // 주문 리스트 작성
        List<OrderItem> newOrderItems = OrderItem.createNewOrderItemsWithNoPriceTag(userId, orderId, purchaseRequestList);
        productComponent.setPriceTag(newOrderItems);

        // 영속성 반영
        newOrderItems = orderItemComponent.save(newOrderItems);

        // 포인트 차감
        pointComponent.subtract(newOrderItems);

        return newOrderItems.stream().map(OrderItem::toDto).toList();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public List<OrderItemDto> cancel(long orderId) throws Exception {
        List<OrderItem> purchaseList = orderItemComponent.canceldByOrderId(orderId);
        pointComponent.cancelSubtract(purchaseList);

        return purchaseList.stream().map(OrderItem::toDto).toList();
    }
}
