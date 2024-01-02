package com.lshh.hhp.order;

import com.lshh.hhp.point.PointBase;
import com.lshh.hhp.product.ProductBase;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.product.ProductDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderBiz2ImplIntegrationTest {

    @Autowired
    private OrderOrchestrator orderService;
    @Autowired
    private PointBase pointComponent;
    @Autowired
    private ProductBase productComponent;

    private List<RequestPurchaseDto> prepareRequestPurchaseDtoList() {
        // 30
        RequestPurchaseDto request1 = new RequestPurchaseDto()
                .setProductId(1L)
                .setCount(1);
        // 5
        RequestPurchaseDto request2 = new RequestPurchaseDto()
                .setProductId(2L)
                .setCount(2);


        // 객체 필드를 채우세요
        return Arrays.asList(request1, request2);
    }

    @Test
    @Order(1)
    @DisplayName("주문 성공 확인")
    public void testOrder() throws Exception {
        long userId = 1L;
        System.out.println("주문 전 남은 잔액: "+pointComponent.remain(userId));
        // 100 - 40
        List<RequestPurchaseDto> purchaseRequestList = prepareRequestPurchaseDtoList();
        OrderDto orderDto = orderService.order(userId, purchaseRequestList);
        System.out.println("주문 후 남은 잔액: "+pointComponent.remain(userId));
        
        assertEquals(1L, orderDto.id());
        assertEquals(Result.SUCCESS, orderDto.state());

        List<OrderDto> orderDtoList = orderService.findByUserId(userId);
        assertTrue(!orderDtoList.isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("주문 확인")
    public void testFindByUserId() throws Exception {
        long userId = 1L;
        List<OrderDto> orderDtoList = orderService.findByUserId(userId);
        assertTrue(!orderDtoList.isEmpty());
    }

    // 동시성 테스트 - 주문 실패 케이스 - 포인트 부족
    @Test
    @Order(4)
    @DisplayName("동시성 - 포인트 부족 - 주문 하나만 실패")
    void orderPointlessWithConcurrentTest() throws Exception {
        long userId = 1L;   // 60

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.range(0, 2)
            .parallel()
            .forEach(i ->
                executorService.submit(() -> {
                    try {
                        orderService.order(userId, prepareRequestPurchaseDtoList());
                    } catch (Exception e) {
                        System.out.println("주문 실패!");
                        System.out.println(e.getMessage());
                    }
                    System.out.println(i + " 남은 잔액: "+pointComponent.remain(userId));
                })
            );
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println("남은 잔액: "+pointComponent.remain(userId));
        assertTrue(pointComponent.remain(userId) >= 0);
    }

    // 동시성 테스트 - 재고 처리 실패 케이스 - 재고 부족
    @Test
    @Order(3)
    @DisplayName("동시성 - 재고 부족 - 주문 하나만 실패")
    void orderStocklessWithConcurrentTest() throws Exception {
        long userId = 1L;   // 20
        RequestPurchaseDto requestLessStockCase = new RequestPurchaseDto()
                .setProductId(3L)   // price 1, cnt 1
                .setCount(1);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.range(0, 2)
            .parallel()
            .forEach(i ->
                executorService.submit(() -> {
                    try {
                        orderService.order(userId, Arrays.asList(requestLessStockCase));
                    } catch (Exception e) {
                        System.out.println("주문 실패!");
                        System.out.println(e.getMessage());
                    }
                    System.out.println(i + " 재고 수량: "+productComponent.find(requestLessStockCase.getProductId()).map(ProductDto::stockCnt).orElse(0));
                })
            );
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println("재고 수량: "+productComponent.find(requestLessStockCase.getProductId()).map(ProductDto::stockCnt).orElse(0));
        assertTrue(productComponent.find(requestLessStockCase.getProductId()).map(ProductDto::stockCnt).orElse(0) >= 0);
    }
}