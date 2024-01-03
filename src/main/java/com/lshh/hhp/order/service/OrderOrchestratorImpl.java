package com.lshh.hhp.order.service;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.dto.event.CancelOrderEvent;
import com.lshh.hhp.order.Order;
import com.lshh.hhp.order.dto.OrderDto;
import com.lshh.hhp.point.service.PointBase;
import com.lshh.hhp.product.service.ProductBase;
import com.lshh.hhp.common.Biz;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.orderItem.service.OrderItemService;
import com.lshh.hhp.user.UserBase;
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
@Biz(level = 2)
public class OrderOrchestratorImpl implements OrderOrchestrator {

    // 규칙. 작은 숫자의 biz1 만을 확장 가능하다.
    final OrderItemService purchaseComponent;

    final OrderComponent orderComponent;
    final UserBase userComponent;
    final PointBase pointComponent;
    final ProductBase productComponent;

    final ApplicationEventPublisher publisher;

    @Override
    @EventListener
    public void onCancelOrderEvent(CancelOrderEvent event) {
        log.info("handle_cancel_order_event");
        try{
            cancel(event.orderId());
        }catch (Exception exception){
            log.error("cancel_order_event_failed");
            log.error(exception.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findByUserId(long userId) {
        return orderComponent
                .findByUserId(userId)
                .stream().map(Order::toDto).toList();
    }

    // order - 동기 주문 처리
    @Override
    public OrderDto order(long userId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {
        // # 0. user 확인
        userComponent.find(userId).orElseThrow(Exception::new);
        // # 1. 주문 생성: 시작
        Order order = Order.createNewOrder(userId);
        try {
            // ## 0. 상품 확인
            if(!productComponent.validate(purchaseRequestList)){
                throw new Exception("잘못된 상품");
            }

            // # 구매 처리
            // ## 1. 구매 처리
            purchaseComponent.purchase(userId, order.id(), purchaseRequestList);
            // ## 2. 상품 재고 처리
            productComponent.deduct(purchaseRequestList);
            // # 3. 주문 완료: 종료
            orderComponent.setState(order.id(), Response.Result.SUCCESS);
            return order.toDto();

        }catch (Exception exception){
            log.error(exception.getMessage());
            orderComponent.setState(order.id(), Response.Result.FAIL);

            log.info("invoke_cancel_order_event");
            publisher.publishEvent(new CancelOrderEvent().orderId(order.id()));
            throw exception;
        }
    }

    // order - 동기 주문 취소 처리
    @Override
    @Lock(LockModeType.OPTIMISTIC)
    @Transactional
    public OrderDto cancel(long orderId) throws Exception {
        // 1. 주문 확인
        Order target = orderComponent.find(orderId).orElseThrow(()->new Exception("잘못된 주문"));

        // 2. 주문 취소 시작
        orderComponent.setState(target.id(), Response.Result.CANCELING);

        try {
            // 3. 구매 취소
            // 4. 포인트 취소
            List<OrderItemDto> canceledList = purchaseComponent.cancel(target.id());

            // 5. 재고 다시 추가
            productComponent.conduct(canceledList);

            // 6. 취소 완료 - 취소 실패시, 이전 상태로
            orderComponent.setState(target.id(), Response.Result.CANCELED);
            return target.toDto();

        }catch (Exception exception){
            log.error(exception.getMessage());
            orderComponent.setState(target.id(), Response.Result.FAIL);
            throw exception;
        }
    }
}
