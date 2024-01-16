package com.lshh.hhp.order.service;

import com.lshh.hhp.order.dto.EventCancelOrderDto;
import com.lshh.hhp.order.dto.OrderDetailDto;
import com.lshh.hhp.product.dto.RequestProductDto;
import com.lshh.hhp.order.dto.OrderDto;

import java.util.List;

/**
 * level 2
 * OrderOrchestratorService is an interface that defines the methods for managing orders and cancellations.
 * It provides functionality to order products, cancel orders, and retrieve orders based on user ID.
 */
// delux biz
public interface OrderOrchestratorService {

    /**
     * Event listener, Cancels an order based on the event provided
     *
     * @param event the CancelOrderEvent object containing the order ID to be canceled
     * @throws Exception if an error occurs during the cancellation process
     */
    void onCancelOrderEvent(EventCancelOrderDto event) throws Exception;

    /**
     * Retrieves a list of OrderDto objects for a given user ID
     *
     * @param userId the ID of the user
     * @return a list of OrderDto objects representing the orders made by the user
     */
    List<OrderDto> findByUserId(long userId);
    
    /**
     * Orders products for a user by sync
     *
     * @param userId    the ID of the user
     * @param productId a list of RequestPurchaseDto objects representing the products to be ordered
     * @return the ordered products as an OrderDto object
     * @throws Exception if an error occurs during the ordering process
     */
    OrderDto order(long userId, List<RequestProductDto> productId) throws Exception;
    // 1. 주문 생성: 시작

    // 2. 상품 확인

    // 3. 상품 재고 확인
    // 4. 상품 재고 처리

    // 5. 아이디 포인트 확인
    // 6. 구매 생성
    // 7. 아이디 포인트 차감

    // 8. 주문 완료: 종료

    // # 주문 취소 동기 처리

    /**
     * Cancels an order by sync
     *
     * @param orderId the ID of the order to be canceled
     * @return the canceled order as an OrderDto object
     * @throws Exception if an error occurs during the cancellation process
     */
    OrderDto cancel(long orderId) throws Exception;

    // 1. 주문 확인 - fail 제외
    // 2. 주문 취소 시작
    
    // 3. 구매 취소
    // 4. 포인트 취소
    // 5. 재고 취소

    // 6. 취소 완료 - 취소 실패시, 이전 상태로


    List<OrderDto> findFailedByUserId(Long userId);

    OrderDetailDto findDetail(Long orderId);
}
