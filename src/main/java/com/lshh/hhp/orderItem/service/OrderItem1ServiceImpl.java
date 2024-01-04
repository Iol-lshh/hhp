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

    final PointService pointService;
    final OrderItemService orderItemService;
    final ProductService productService;

    @Override
    @Cacheable
    public List<ViewPurchasedProductDto> favorite(Integer count) {
        return orderItemService.favorite(count);
    }

    @Override
    @Transactional
    public List<OrderItemDto> orderEachProduct(long userId, long orderId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {
        // 주문 리스트 작성
        List<OrderItem> newOrderItems = OrderItem.createNewOrderItemsYetNoPrice(userId, orderId, purchaseRequestList);
        productService.putPrice(newOrderItems);
        newOrderItems = orderItemService.save(newOrderItems);

        // 포인트 차감
        pointService.subtract(newOrderItems);

        return newOrderItems.stream().map(OrderItem::toDto).toList();
    }

    @Override
    @Transactional
    public List<OrderItemDto> cancel(long orderId) throws Exception {
        List<OrderItem> purchaseList = orderItemService.canceldByOrderId(orderId);
        pointService.cancelSubtract(purchaseList);

        return purchaseList.stream().map(OrderItem::toDto).toList();
    }
}
