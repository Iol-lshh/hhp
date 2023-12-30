package com.lshh.hhp.order;

import com.lshh.hhp.point.PointBiz;
import com.lshh.hhp.product.ProductBiz;
import com.lshh.hhp.common.Biz;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.dto.event.CancelPurchasedOrderEvent;
import com.lshh.hhp.purchase.PurchaseDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.purchase.PurchaseBiz1;
import com.lshh.hhp.dto.event.PurchaseOrderEvent;
import com.lshh.hhp.user.UserBiz;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Biz(level = 2)
public class OrderOrchestratorImpl implements OrderOrchestrator {

    // 규칙. 작은 숫자의 biz1 만을 확장 가능하다.
    final PurchaseBiz1 purchaseComponent;

    final OrderBiz orderComponent;
    final UserBiz userComponent;
    final PointBiz pointComponent;
    final ProductBiz productComponent;

    final ApplicationEventPublisher publisher;

    // order - 강결합 동기 주문 처리
    @Override
    @Transactional
    public OrderDto order(long userId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {
        // # 0. user 확인
        userComponent.find(userId).orElseThrow(Exception::new);
        // # 1. 주문 생성: 시작
        OrderDto order = orderComponent.start(userId);
        try {
            // ## 0. 상품 확인
            if(!productComponent.validate(purchaseRequestList)){
                throw new Exception("잘못된 상품");
            }

            // # 구매 처리
            // ## 1. 상품 재고 처리
            productComponent.unstore(purchaseRequestList);
            // ## 2. 구매 처리
            purchaseComponent.purchase(userId, order.id(), purchaseRequestList);
            // # 3. 주문 완료: 종료
            return orderComponent.success(order);
        }catch (Exception err){
            orderComponent.fail(order);
            throw err;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findByUserId(long userId) {
        return orderComponent
            .findByUserId(userId);
    }

    // order - 강결합 동기 주문 취소 처리
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public OrderDto cancel(long orderId) throws Exception {
        // 1. 주문 확인 - fail 제외
        OrderDto target = orderComponent.find(orderId).orElseThrow(()->new Exception("잘못된 주문"));
        Result beforeState = target.state();
        if(beforeState.equals(Result.FAIL)){
            throw new Exception("실패한 주문");
        }

        // 2. 주문 취소 시작
        target = orderComponent.startCancel(target);

        // 3. 구매 취소
        // 4. 포인트 취소
        List<PurchaseDto> canceledList = purchaseComponent.cancel(target.id());

        // 5. 재고 다시 추가
        productComponent.restore(canceledList);

        // 6. 취소 완료 - 취소 실패시, 이전 상태로
        return orderComponent.finishedCancel(target);
    }

    // 약결합 - 주문 시작 처리
    @Override
    public OrderDto startOrder(long userId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {
        // # 0. user 확인
        userComponent.find(userId).orElseThrow(Exception::new);
        // # 1. 주문 생성: 시작
        OrderDto order = orderComponent.start(userId);
        // # 2. 상품 확인
        if (!productComponent.validate(purchaseRequestList)) {
            throw new Exception("잘못된 상품");
        }

        // 주문 발행 - fire and forget
        publisher.publishEvent(
            new PurchaseOrderEvent()
                .userId(userId)
                .orderId(order.id())
                .purchaseRequestList(purchaseRequestList)
        );

        return order;
    }

    @Override
    public OrderDto success(long orderId) {
        return orderComponent.success(new OrderDto().id(orderId));
    }

    @Override
    public OrderDto fail(long orderId) {
        return orderComponent.fail(new OrderDto().id(orderId));
    }

    @Override
    public OrderDto startCancel(long orderId) {

        // 주문 취소 발행
        publisher.publishEvent(
            new CancelPurchasedOrderEvent()
                .orderId(orderId)
        );

        return orderComponent.startCancel(new OrderDto().id(orderId));
    }

    @Override
    public OrderDto finishedCancel(long orderId) {
        return orderComponent.finishedCancel(new OrderDto().id(orderId));
    }
}
