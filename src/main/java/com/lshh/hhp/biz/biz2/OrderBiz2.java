package com.lshh.hhp.biz.biz2;

import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.OrderDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;

import java.util.List;

public interface OrderBiz2 {

    ResultDto<OrderDto> order(long userId, List<RequestPurchaseDto> productId) throws Exception;
    // 1. 주문 생성: 시작
    // 1. 재고 확인
    //  1. 아이디 포인트 확인
    //  2. 상품 재고 확인
    // 2. 구매 처리
    //  1. 구매 생성
    //  2. 아이디 포인트 차감
    //  3. 상품 재고 처리
    // 2. 주문 완료: 종료

    boolean isPayable(long userId, List<RequestPurchaseDto> requestList);

    List<OrderDto> findByUserId(long userId);
}
