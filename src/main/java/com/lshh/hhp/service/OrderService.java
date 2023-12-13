package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.OrderDto;
import com.lshh.hhp.dto.PurchaseRequestDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    ResultDto<OrderDto> order(long userId, List<PurchaseRequestDto> productId) throws Exception;
    // 1. 주문 생성: 시작
    // 1. 재고 확인
    //  1. 아이디 포인트 확인
    //  2. 상품 재고 확인
    // 2. 구매 처리
    //  1. 구매 생성
    //  2. 아이디 포인트 차감
    //  3. 상품 재고 처리
    // 2. 주문 완료: 종료

    List<OrderDto> findAll();
    Optional<OrderDto> find(long id);

    List<OrderDto> findByUserId(long userId);
}
