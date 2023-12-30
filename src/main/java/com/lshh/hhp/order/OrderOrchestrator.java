package com.lshh.hhp.order;

import com.lshh.hhp.dto.request.RequestPurchaseDto;

import java.util.List;

// delux biz
public interface OrderOrchestrator {
    
    List<OrderDto> findByUserId(long userId);
    
    // 주문 동기 처리
    OrderDto order(long userId, List<RequestPurchaseDto> productId) throws Exception;
    // 1. 주문 생성: 시작

    // 2. 상품 확인

    // 3. 상품 재고 확인
    // 4. 상품 재고 처리

    // 5. 아이디 포인트 확인
    // 6. 구매 생성
    // 7. 아이디 포인트 차감

    // 8. 주문 완료: 종료

    // 주문 취소 동기 처리
    OrderDto cancel(long orderId) throws Exception;
    // 1. 주문 확인 - fail 제외
    // 2. 주문 취소 시작
    
    // 3. 구매 취소
    // 4. 포인트 취소
    // 5. 재고 취소

    // 6. 취소 완료 - 취소 실패시, 이전 상태로

    
    // 주문 이벤트 발급
    OrderDto startOrder(long userId, List<RequestPurchaseDto> purchaseRequestList) throws Exception;

    OrderDto success(long orderId);
    OrderDto fail(long orderId);

    OrderDto startCancel(long orderId);
    OrderDto finishedCancel(long orderId);

}
