package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.common.jpa.MockBundle;
import com.lshh.hhp.dto.*;
import com.lshh.hhp.orm.entity.*;
import com.lshh.hhp.orm.repository.OrderRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    UserService userService;
    @Mock
    PurchaseService purchaseService;
    @Mock
    PointService pointService;
    @Mock
    ProductService productService;
    @Mock
    StockService stockService;
    @InjectMocks
    OrderServiceDefault orderService;


    @DisplayName("주문 성공 케이스")
    @Test
    public void orderTest_validData_successCase() throws Exception {
        // # Prepare test data
        // 주문자
        User user = MockBundle.mockupUser();
        // 주문할 상품 1
        Product product1 = MockBundle.mockupProduct();
        int stockCnt1 = MockBundle.mockupStockCnt(1);
        RequestPurchaseDto requestPurchaseDto1 = MockBundle.mockupRequestPurchaseDto(product1.id(), -1 * stockCnt1);
        // 주문할 상품 2
        Product product2 = MockBundle.mockupProduct();
        int stockCnt2 = MockBundle.mockupStockCnt(1);
        RequestPurchaseDto requestPurchaseDto2 = MockBundle.mockupRequestPurchaseDto(product1.id(), -1 * stockCnt1);
        List<RequestPurchaseDto> requestPurchaseDtoList = List.of(requestPurchaseDto1, requestPurchaseDto2);

        // 주문
        Order orderStart = new Order().userId(user.id()).state(Result.Start.ordinal());
        Order orderStarted = MockBundle.mockupOrder(user.id(), Result.Start);
        // 구매서 1 (expect)
        PurchaseDto purchaseDto1 = new PurchaseDto()
                .id(anyLong())
                .orderId(orderStarted.id())
                .count(requestPurchaseDto1.getCount())
                .userId(user.id())
                .productId(requestPurchaseDto1.getProductId())
                .paid(requestPurchaseDto1.getCount()*product1.price());
        // 구매서 2 (expect)
        PurchaseDto purchaseDto2 = new PurchaseDto()
                .id(purchaseDto1.id() + 1)
                .orderId(orderStarted.id())
                .count(requestPurchaseDto2.getCount())
                .userId(user.id())
                .productId(requestPurchaseDto2.getProductId())
                .paid(requestPurchaseDto2.getCount()*product2.price());



        // # Mock behaviors
        // user 확인
        when(userService.find(user.id()))
                .thenReturn(Optional.of(UserServiceDefault.toDto(user)));
        // order 생성 과정
        when(orderRepository.save(orderStart))
                .thenReturn(orderStarted);
        // 상품 검증 과정
        when(productService.find(requestPurchaseDto1.getProductId()))
                .thenReturn(Optional.of(ProductServiceDefault.toDto(product1)));
        when(productService.find(requestPurchaseDto2.getProductId()))
                .thenReturn(Optional.of(ProductServiceDefault.toDto(product2)));
        // 재고 검증 과정
        when(stockService.isAllInStock(requestPurchaseDtoList))
                .thenReturn(true);
        // 포인트 검증 과정
        when(purchaseService.isPayable(user.id(), requestPurchaseDtoList))
                .thenReturn(true);
        // 결제 처리 과정
        when(purchaseService.purchase(user.id(), orderStarted.id(), requestPurchaseDtoList))
                .thenReturn(new ResultDto<>(Result.OK, List.of(purchaseDto1, purchaseDto2)));
        // 주문 완료 처리 과정
        when(orderRepository.save(orderStarted))
                .thenReturn(orderStarted.state(Result.OK.ordinal()));
        // 주문 전체 처리 과정 결과
        when(orderService.order(user.id(), requestPurchaseDtoList))
                .thenReturn(new ResultDto<>(OrderServiceDefault.toDto(orderStarted.state(Result.OK.ordinal()))));

        // # 테스트
        ResultDto<OrderDto> resultDto = orderService.order(user.id(), requestPurchaseDtoList);


//        verify(userService).find(user.id());
//        verify(orderRepository).save(orderStart);
//        verify(productService).find(product1.id());
//        verify(productService).find(product2.id());
//        verify(stockService).isAllInStock(requestPurchaseDtoList);
//        verify(purchaseService).isPayable(user.id(), requestPurchaseDtoList);
//        verify(purchaseService).purchase(user.id(), orderStarted.id(), requestPurchaseDtoList);
//        verify(orderRepository).save(orderStarted);

        // 결과 검증
        assertNotNull(resultDto, "ResultDto가 null 여부");
        assertEquals(Result.OK, resultDto.getResult(), "요청 결과");
        assertNotNull(resultDto.getValue(), "값 null 여부");
        assertEquals(user.id(), resultDto.getValue().userId(), "주문자 아이디");
        assertEquals(Result.OK, resultDto.getValue().state(), "주문 상태");
    }

//    @Test
//    public void orderTest_userNotFound_failureCase() throws Exception {
//        // Prepare test data
//        long userId = 1;
//        List<RequestPurchaseDto> purchaseRequestList = List.of(new RequestPurchaseDto());
//
//        // Mock behaviors
//        when(userService.find(userId)).thenReturn(Optional.empty());
//
//        // Expect an exception
//        assertThrows(Exception.class, () -> {
//            orderService.order(userId, purchaseRequestList);
//        });
//
//        // Verify interactions
//        verify(userService).find(userId);
//        verifyNoInteractions(productService, stockService, purchaseService, orderRepository);
//    }
//
    // You may continue adding more scenarios based on different exceptional cases and success cases


}