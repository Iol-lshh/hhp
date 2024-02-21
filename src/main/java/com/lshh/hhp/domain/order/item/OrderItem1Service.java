package com.lshh.hhp.domain.order.item;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.common.annotation.Service;
import com.lshh.hhp.domain.product.dto.RequestProductDto;
import com.lshh.hhp.domain.point.PointService;
import com.lshh.hhp.domain.product.Product;
import com.lshh.hhp.domain.product.dto.ProductDto;
import com.lshh.hhp.domain.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service(level = 1)
public class OrderItem1Service {

    final OrderItemRepository orderItemRepository;
    final OrderItemService orderItemService;
    final PointService pointService;
    final ProductService productService;

    @Cacheable(cacheNames = "favorite", key = "'Top' + #count")
    public List<ProductDto> favorite(Integer count) {
        List<Product> favoriteList = productService.favorite(count);
        return favoriteList.stream().map(Product::toDto).toList();
    }
//    public List<ViewPurchasedProductDto> favorite(Integer count) {
//        return orderItemService.favorite(count);
//    }

    @Transactional
    public List<OrderItem> orderEachProduct(long userId, long orderId, List<RequestProductDto> purchaseRequestList) throws Exception {
        // 주문 리스트 작성
        List<OrderItem> newOrderItems = OrderItem.createNewOrderItemsYetNoPrice(userId, orderId, purchaseRequestList);
        productService.putPrice(newOrderItems);
        newOrderItems = orderItemRepository.saveAll(newOrderItems);
        try{
            // 포인트 차감
            pointService.subtractByOrderItems(userId, newOrderItems);

            // ## 2. 상품 재고 처리
            productService.deduct(newOrderItems);

            newOrderItems.forEach(orderItem -> orderItem.setState(Response.Result.SUCCESS.ordinal()));

        }catch (Exception exception){
            newOrderItems.forEach(orderItem -> orderItem.setState(Response.Result.FAIL.ordinal()));
            throw exception;

        }finally {
            orderItemRepository.saveAll(newOrderItems);

        }
        return newOrderItems;
    }

    @Transactional
    public List<OrderItem> cancelOrderItem(long userId, long orderId) throws Exception {
        List<OrderItem> targetList = orderItemRepository.findByOrderId(orderId);
        targetList.forEach(target -> target.setState(Response.Result.CANCELING.ordinal()));

        try{
            // 포인트 캔슬
            pointService.subtractByOrderItems(userId, targetList);

            // 재고 다시 추가
            productService.conduct(targetList);

            targetList.forEach(target -> target.setState(Response.Result.CANCELED.ordinal()));

        }catch (Exception exception){
            targetList.forEach(target -> target.setState(Response.Result.FAIL.ordinal()));
            throw exception;

        }finally {
            orderItemRepository.saveAll(targetList);

        }
        return targetList;
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
