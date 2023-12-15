package com.lshh.hhp.common.jpa;


import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.RequestPurchaseDto;
import com.lshh.hhp.orm.entity.Order;
import com.lshh.hhp.orm.entity.Product;
import com.lshh.hhp.orm.entity.Stock;
import com.lshh.hhp.orm.entity.User;

import java.util.Random;

public class MockBundle {

    private static String anyString(){
        String tmp = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = new Random().nextInt(tmp.length());
            sb.append(tmp.charAt(index));
        }
        return sb.toString();
    }
    private static long anyLong(){
        return Math.abs(new Random().nextLong());
    }

    /**
     * @return 임의의 유저 객체
     */
    public static User mockupUser(){
        return new User()
                .id(anyLong())
                .name(anyString());
    }

    /**
     * @param addPoint 반환값의 절대값이, 인자가 양수면 양수보다 크다, 음수면 음수보다 작다
     * @return 임의의 잔여 포인트
     */
    public static int mockupRemainPoint(int addPoint){
        return (int) (anyLong() + addPoint);
    }

    /**
     * @param productId 구매 요청하는 상품 아이디
     * @param addCount 반환값의 절대값이, 인자가 양수면 양수보다 크다, 음수면 음수보다 작다
     * @return 구매 요청 DTO
     */
    public static RequestPurchaseDto mockupRequestPurchaseDto(long productId, int addCount){
        return new RequestPurchaseDto()
                .setProductId(productId)
                .setCount((int) (anyLong() + addCount));    //
    }

    /**
     * @param state 주문의 상태 (Start, OK, FAIL)
     * @param userId 주문자 id
     * @return 임의의 주문 객체
     */
    public static Order mockupOrder(long userId, Response.Result state){
        return new Order()
                .id(anyLong())
                .userId(userId)
                .state(state.ordinal());
    }

    /**
     * @return 임의의 상품 객체
     */
    public static Product mockupProduct(){
        return new Product()
                .id(anyLong())
                .name(anyString())
                .price((int) anyLong());
    }

    /**
     * @param productId 상품 아이디
     * @param purchaseId 구매정보 아이디. 0이 아닐 경우, 판매 처리
     * @return 임의의 재고 객체
     */
    public static Stock mockupStock(long productId, Long purchaseId){
        Stock stock = new Stock()
                .id(anyLong())
                .productId(productId);

        // 구매시, purchaseId 입력
        if(purchaseId == 0){
            return stock;
        }
        return stock.purchaseId(purchaseId);
    }

    /**
     * @param addCnt 반환값의 절대값이, 인자가 양수면 양수보다 크다, 음수면 음수보다 작다
     * @return 임의의 재고 갯수
     */
    public static int mockupStockCnt(int addCnt){
        return (int) (anyLong() + addCnt);  //
    }
}