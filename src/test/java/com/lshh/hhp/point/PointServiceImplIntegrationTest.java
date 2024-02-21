package com.lshh.hhp.point;

import com.lshh.hhp.domain.order.item.OrderItem;
import com.lshh.hhp.domain.point.PointService;
import com.lshh.hhp.domain.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

//@ActiveProfiles("test")
@SpringBootTest
public class PointServiceImplIntegrationTest {
    
    @Autowired
    private PointService pointService;
    @Autowired
    private ProductService productService;
    
    @Test
    @DisplayName("동시성 - 포인트 동시 결제 1건만 성공")
    public void testSubtract() throws Exception {
        // optimistic lock - 1건만 처리
        // Initialization
        long testUserId = 1L;
        OrderItem orderItem2 = OrderItem.createNewOrderItemWithNoPriceTag(testUserId, 1L, 1L, 1);
        List<OrderItem> orderItems = Arrays.asList(orderItem2);
        productService.putPrice(orderItems);
        int sumToPay = orderItems.stream().mapToInt(OrderItem::toPay).sum();
        System.out.println("구매금액 "+ sumToPay);
        // Performing the test
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        IntStream.range(0, 4)
                .parallel()
                .forEach(i ->
                        executorService.submit(() -> {
                            try {
                                System.out.println(i + " a남은 잔액: "+pointService.countRemain(testUserId));
                                pointService.subtractByOrderItems(testUserId, orderItems);
                                System.out.println(i + " 차감 성공! b남은 잔액: "+pointService.countRemain(testUserId));
                            } catch (Exception e) {
                                System.out.println(i + " 차감 실패! c남은 잔액: "+pointService.countRemain(testUserId));
                                System.out.println(e.getMessage());
                            }
                            System.out.println(i + " d남은 잔액: "+pointService.countRemain(testUserId));
                        })
                );
        executorService.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("남은 잔액: "+pointService.countRemain(testUserId));
        assertTrue(pointService.countRemain(testUserId) >= 0);
    }


}


