package com.lshh.hhp.order;

import com.lshh.hhp.order.dto.OrderDto;
import com.lshh.hhp.order.service.OrderService;
import com.lshh.hhp.order.service.OrderOrchestratorServiceImpl;
import com.lshh.hhp.product.service.ProductService;
import com.lshh.hhp.user.User;
import com.lshh.hhp.user.service.UserService;
import com.lshh.hhp.orderItem.service.OrderItem1Service;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.product.dto.ProductDto;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderBiz2ImplTest {

    @InjectMocks
    private OrderOrchestratorServiceImpl orderOrchestratorService;
    @Mock
    private OrderService orderService;

    @Mock
    private OrderItem1Service orderItem1Service;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;

    @Mock
    private ApplicationEventPublisher publisher;


    long testUserId = 1L;
    RequestPurchaseDto mockedPurchaseDto;
    OrderDto mockedOrderDto;
    List<RequestPurchaseDto> mockedPurchaseList;
    OrderItemDto mockedPurchasedDto;
    List<OrderItemDto> mockedPurchasedList;

    @BeforeEach
    public void setup() {
        this.mockedPurchaseDto = new RequestPurchaseDto().setCount(1);
        this.mockedOrderDto = new OrderDto(1L, this.testUserId, Result.START);
        this.mockedPurchaseList = Arrays.asList(this.mockedPurchaseDto);
        this.mockedPurchasedDto = new OrderItemDto()
                .id(1L)
                .orderId(mockedOrderDto.id())
                .userId(testUserId)
                .count(mockedPurchaseDto.getCount())
                .productId(mockedPurchaseDto.getProductId())
                .toPay(11);
        this.mockedPurchasedList = Arrays.asList(mockedPurchasedDto);
    }

    // findByUserId
    @Test
    @DisplayName("유저 주문 정보 조회")
    public void findByUserId() {
        Order mockedOrder = Order.toEntity(mockedOrderDto);

        List<Order> expectedOrderDtos = new ArrayList<>();
        expectedOrderDtos.add(Order.toEntity(mockedOrderDto));

        when(orderService.findByUserId(Mockito.anyLong())).thenReturn(List.of(mockedOrder));

        // Act
        List<OrderDto> result = orderOrchestratorService.findByUserId(testUserId);

        // Assert
        assert expectedOrderDtos.get(0).userId().equals(result.get(0).id());
    }


    // # order
    @Test
    @DisplayName("order: 주문 가능한 경우")
    public void order() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);

        when(userService.find(testUserId)).thenReturn(Optional.of(new User()));
        when(orderService.start(testUserId)).thenReturn(mockedOrder);
        when(productService.validate(mockedPurchaseList)).thenReturn(true);
        doReturn(Arrays.asList(mockedPurchasedDto)).when(orderItem1Service).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        doReturn(new ArrayList<ProductDto>()).when(productService).deduct(mockedPurchaseList);

        OrderDto result = orderOrchestratorService.order(testUserId, mockedPurchaseList);

        // Assert
        assertEquals(result.id(), mockedOrderDto.id());
        verify(productService, times(1)).validate(mockedPurchaseList);
        verify(orderItem1Service, times(1)).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        verify(productService, times(1)).deduct(mockedPurchaseList);
    }

    @Test
    @DisplayName("order: 주문중 - 상품 조회 불가한 경우")
    public void order_whenProductNotValid_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);

        when(userService.find(testUserId)).thenReturn(Optional.of(new User()));
        when(orderService.start(testUserId)).thenReturn(mockedOrder);
        //
        when(productService.validate(mockedPurchaseList)).thenReturn(false);

        // Assert
        Exception exception = assertThrows(Exception.class, () ->
                orderOrchestratorService.order(testUserId, mockedPurchaseList));
        assertEquals("잘못된 상품", exception.getMessage());

        verify(productService, times(1)).validate(mockedPurchaseList);
    }

    @Test
    @DisplayName("order: 주문중 - 지불 불가하여, 구매 처리 실패한 경우")
    public void order_whenProductOutOfStock_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);

        when(userService.find(testUserId)).thenReturn(Optional.of(new User()));
        when(orderService.start(testUserId)).thenReturn(mockedOrder);
        when(productService.validate(mockedPurchaseList)).thenReturn(true);
        //
        when(orderItem1Service.orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList)).thenThrow(new Exception("포인트 부족"));

        // Assert
        Exception exception = assertThrows(Exception.class, () ->
                orderOrchestratorService.order(testUserId, mockedPurchaseList));
        assertEquals("포인트 부족", exception.getMessage());

        verify(productService, times(1)).validate(mockedPurchaseList);
        verify(orderItem1Service, times(1)).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
    }

    @Test
    @DisplayName("order: 주문중 - 상품 재고 부족하여, 재고 처리 실패한 경우")
    public void order_whenNotPayable_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);

        when(userService.find(testUserId)).thenReturn(Optional.of(new User()));
        when(orderService.start(testUserId)).thenReturn(mockedOrder);
        when(productService.validate(mockedPurchaseList)).thenReturn(true);
        doReturn(Arrays.asList(mockedPurchasedDto)).when(orderItem1Service).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        //
        when(productService.deduct(mockedPurchaseList)).thenThrow(new Exception("재고 부족"));

        // Assert
        Exception exception = assertThrows(Exception.class, () ->
                orderOrchestratorService.order(testUserId, mockedPurchaseList));
        assertEquals("재고 부족", exception.getMessage());

        verify(productService, times(1)).validate(mockedPurchaseList);
        verify(orderItem1Service, times(1)).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        verify(productService, times(1)).deduct(mockedPurchaseList);
    }

    // # cancel
    @Test
    @DisplayName("cancel: 주문 취소")
    public void cancel() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);

        when(orderService.find(mockedOrderDto.id())).thenReturn(Optional.of(mockedOrder));
        when(orderItem1Service.cancel(mockedOrderDto.id())).thenReturn(mockedPurchasedList);
        doReturn(new ArrayList<ProductDto>()).when(productService).conduct(mockedPurchasedList);

        OrderDto result = orderOrchestratorService.cancel(mockedOrderDto.id());

        //todo fix err
        assertEquals(result.id(), mockedOrderDto.id());
        assertEquals(result.state(), Result.CANCELED);
        verify(orderService, times(1)).find(mockedOrderDto.id());
        verify(orderItem1Service, times(1)).cancel(mockedOrderDto.id());
        verify(productService, times(1)).conduct(mockedPurchasedList);
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 주문 정보가 없는 경우")
    public void cancel_whenOrderNotFound_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);
        when(orderService.find(mockedOrderDto.id())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () ->
                orderOrchestratorService.cancel(mockedOrderDto.id()));
        assertEquals("잘못된 주문", exception.getMessage());

        verify(orderService, times(1)).find(mockedOrderDto.id());
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 구매 취소 실패한 경우")
    public void cancel_whenPurchaseCancelFailed_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);

        when(orderService.find(mockedOrderDto.id())).thenReturn(Optional.of(mockedOrder));
        when(orderItem1Service.cancel(mockedOrderDto.id())).thenThrow(new Exception("구매 취소 실패"));

        Exception exception = assertThrows(Exception.class, () ->
                orderOrchestratorService.cancel(mockedOrderDto.id()));
        assertEquals("구매 취소 실패", exception.getMessage());

        verify(orderService, times(1)).find(mockedOrderDto.id());
        verify(orderItem1Service, times(1)).cancel(mockedOrderDto.id());
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 재고 취소 실패한 경우")
    public void cancel_whenUnstoreFailed_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);

        when(orderService.find(mockedOrderDto.id())).thenReturn(Optional.of(mockedOrder));
        when(orderItem1Service.cancel(mockedOrderDto.id())).thenReturn(mockedPurchasedList);
        when(productService.conduct(mockedPurchasedList)).thenThrow(new Exception("재고 취소 실패"));

        Exception exception = assertThrows(Exception.class, () ->
                orderOrchestratorService.cancel(mockedOrderDto.id()));
        assertEquals("재고 취소 실패", exception.getMessage());

        verify(orderService, times(1)).find(mockedOrderDto.id());
        verify(orderItem1Service, times(1)).cancel(mockedOrderDto.id());
        verify(productService, times(1)).conduct(mockedPurchasedList);
    }

}

