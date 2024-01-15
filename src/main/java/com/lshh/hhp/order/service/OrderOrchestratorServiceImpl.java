package com.lshh.hhp.order.service;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.common.exception.BusinessException;
import com.lshh.hhp.order.dto.EventCancelOrderDto;
import com.lshh.hhp.order.Order;
import com.lshh.hhp.order.dto.OrderDto;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.product.service.ProductService;
import com.lshh.hhp.common.Service;
import com.lshh.hhp.product.dto.RequestProductDto;
import com.lshh.hhp.orderItem.service.OrderItem1Service;
import com.lshh.hhp.user.service.UserService;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service(level = 2)
public class OrderOrchestratorServiceImpl implements OrderOrchestratorService {

    // 규칙. 작은 레벨의 service 만을 확장한다.
    final OrderItem1Service orderItem1Service;

    final OrderService orderService;
    final UserService userService;
    final ProductService productService;

    final ApplicationEventPublisher publisher;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findByUserId(long userId) {
        return orderService
                .findByUserId(userId)
                .stream().map(Order::toDto).toList();
    }

    // order - 동기 주문 처리
    @Override
    public OrderDto order(long userId, List<RequestProductDto> requestProductDtos) throws Exception {
        // # 0. user 확인
        userService.find(userId).orElseThrow(()->new BusinessException(String.format("""
                    OrderOrchestratorServiceImpl::order - 잘못된 유저 {%s}""", userId)));

        // # 1. 주문 생성: 시작
        Order order = orderService.start(userId);
        try {
            // ## 0. 상품 확인
            if(!productService.validate(requestProductDtos)){
                throw new BusinessException(String.format("""
                    OrderOrchestratorServiceImpl::order - 잘못된 상품 {%s}""", String.join(","
                        ,requestProductDtos.stream().map(e->e.getProductId().toString()).toList())));
            }

            // # 구매 처리
            // ## 1. 구매 처리
            List<OrderItem> orderItems = orderItem1Service.orderEachProduct(userId, order.id(), requestProductDtos);

            // ## 2. 상품 재고 처리
            productService.deduct(orderItems);

            // # 3. 주문 완료: 종료
            order.setState(Response.Result.SUCCESS);
            return order.toDto();

        }catch (Exception exception){
            order.setState(Response.Result.FAIL);
            publisher.publishEvent(new EventCancelOrderDto().orderId(order.id()));
            throw exception;

        }finally {
            orderService.save(order);
        }
    }


    @Override
    @EventListener
    public void onCancelOrderEvent(EventCancelOrderDto event) throws Exception {
        cancel(event.orderId());
    }

    // order - 동기 주문 취소 처리
    @Override
    @Transactional
    public OrderDto cancel(long orderId) throws Exception {
        // 1. 주문 확인
        Order target = orderService.find(orderId).orElseThrow(()->new BusinessException(String.format("""
        PaymentDto::exchange {"orderId": "%s"}""", orderId)));

        // 2. 주문 취소 시작
//        target.setState(Response.Result.CANCELING);

        try {
            // 3. 구매 취소
            // 4. 포인트 취소
            List<OrderItem> canceledList = orderItem1Service.cancelOrderItem(target.id());

            // 5. 재고 다시 추가
            productService.conduct(canceledList);

            // 6. 취소 완료 - 취소 실패시, 이전 상태로
            target.setState(Response.Result.CANCELED);
            return target.toDto();

        }catch (Exception exception){
            target.setState(Response.Result.FAIL);
            throw exception;

        }finally {
            orderService.save(target);
        }
    }

    @Override
    public List<OrderDto> findFailedByUserId(Long userId) {
        return orderService.findFailedByUserId(userId).stream().map(Order::toDto).toList();
    }

    @Override
    public List<OrderDto> findFailedDetailByUserId(Long userId) {
        return null;
    }
}
