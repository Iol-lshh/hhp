package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.common.Service;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.order.dto.RequestPurchaseDto;
import com.lshh.hhp.orderItem.repository.OrderItemRepository;
import com.lshh.hhp.point.service.PointService;
import com.lshh.hhp.product.Product;
import com.lshh.hhp.product.dto.ProductDto;
import com.lshh.hhp.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service(level = 1)
public class OrderItem1ServiceImpl implements OrderItem1Service {

    final OrderItemRepository orderItemRepository;
    final PointService pointService;
    final ProductService productService;

    @Override
    @Cacheable(cacheNames = "favorite", key = "'Top' + #count")
    public List<ProductDto> favorite(Integer count) {
        List<Product> favoriteList = productService.favorite(count);
        return favoriteList.stream().map(Product::toDto).toList();
    }
//    public List<ViewPurchasedProductDto> favorite(Integer count) {
//        return orderItemService.favorite(count);
//    }

    @Override
    @Transactional
    public List<OrderItem> orderEachProduct(long userId, long orderId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {
        // 주문 리스트 작성
        List<OrderItem> newOrderItems = OrderItem.createNewOrderItemsYetNoPrice(userId, orderId, purchaseRequestList);
        productService.putPrice(newOrderItems);

        newOrderItems = orderItemRepository.saveAll(newOrderItems);
        // 포인트 차감
        pointService.subtractByOrderItems(newOrderItems);

        return newOrderItems;
    }

    @Override
    @Transactional
    public List<OrderItem> cancelOrderItem(long orderId) throws Exception {
        List<OrderItem> targetList = orderItemRepository.findByOrderId(orderId);
        targetList.stream().forEach(target -> target.setState(Response.Result.CANCELING.ordinal()));

        try{
            // 포인트 캔슬
            pointService.cancelSubtract(targetList);
            targetList.stream().forEach(target -> target.setState(Response.Result.CANCELED.ordinal()));

        }catch (Exception exception){
            targetList.stream().forEach(target -> target.setState(Response.Result.FAIL.ordinal()));
            throw exception;
        }

        orderItemRepository.saveAll(targetList);
        return targetList;
    }
}
