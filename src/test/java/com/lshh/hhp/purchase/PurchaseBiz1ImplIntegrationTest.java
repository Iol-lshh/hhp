package com.lshh.hhp.purchase;

import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.point.PointBase;
import com.lshh.hhp.product.ProductBase;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PurchaseBiz1ImplIntegrationTest {

    @Autowired
    PurchaseService purchaseService;
    @Autowired
    PointBase pointComponent;
    @Autowired
    PurchaseBase purchaseComponent;
    @Autowired
    ProductBase productComponent;

    static long orderId = 0L;

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
    @DisplayName("구매 성공")
    void purchase() throws Exception {
        long testUserId = 1L;
        long testOrderId = ++orderId;
        List<RequestPurchaseDto> requests = prepareRequestPurchaseDtoList();
        // Act
        System.out.println("주문 전 남은 잔액: "+pointComponent.remain(testUserId));
        List<PurchaseDto> result = purchaseService.purchase(testUserId, testOrderId, requests);
        System.out.println("주문 후 남은 잔액: "+pointComponent.remain(testUserId));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(pointComponent.remain(testUserId) >= 0);
        //100 - 40 = 60
    }

    // 동시성 테스트 - 주문 실패 케이스 - 포인트 부족
    @Test
    @Order(2)
    @DisplayName("동시성 - 포인트 부족-하나만 실패")
    void orderPointlessWithConcurrentTest() throws Exception {
        long testUserId = 1L;   // 60
        //60 - 40(성공) - 40(실패)

        orderId++;
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.range(0, 2)
                .parallel()
                .forEach(i ->
                        executorService.submit(() -> {
                            long testOrderId = orderId + i;
                            try {
                                purchaseService.purchase(testUserId, testOrderId, prepareRequestPurchaseDtoList());
                            } catch (Exception e) {
                                System.out.println("주문 실패!");
                                System.out.println(e.getMessage());
                            }
                            System.out.println(i + " 남은 잔액: "+pointComponent.remain(testUserId));
                        })
                );
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println("남은 잔액: "+pointComponent.remain(testUserId));
        assertTrue(pointComponent.remain(testUserId) >= 0);
    }

}
