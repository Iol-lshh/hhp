package com.lshh.hhp.order;

import com.lshh.hhp.common.exception.BusinessException;
import com.lshh.hhp.order.dto.OrderDto;
import com.lshh.hhp.order.service.OrderService;
import com.lshh.hhp.order.service.OrderOrchestratorServiceImpl;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.product.service.ProductService;
import com.lshh.hhp.user.User;
import com.lshh.hhp.user.service.UserService;
import com.lshh.hhp.orderItem.service.OrderItem1Service;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.product.dto.ProductDto;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.order.dto.RequestPurchaseDto;
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
    OrderItemDto mockedOrderItemDto;
    List<OrderItemDto> mockedOrderItemDtos;

    @BeforeEach
    public void setup() {
        this.mockedPurchaseDto = new RequestPurchaseDto().setCount(1).setProductId(1L);
        this.mockedOrderDto = new OrderDto(1L, this.testUserId, Result.START);
        this.mockedPurchaseList = Arrays.asList(this.mockedPurchaseDto);
        this.mockedOrderItemDto = new OrderItemDto()
                .id(1L)
                .orderId(mockedOrderDto.id())
                .userId(testUserId)
                .count(mockedPurchaseDto.getCount())
                .productId(mockedPurchaseDto.getProductId())
                .toPay(11);
        this.mockedOrderItemDtos = Arrays.asList(mockedOrderItemDto);
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
        List<OrderItem> orderItems = mockedOrderItemDtos.stream().map(OrderItem::toEntity).toList();

        when(userService.find(testUserId)).thenReturn(Optional.of(new User()));
        when(orderService.start(testUserId)).thenReturn(mockedOrder);
        when(productService.validate(mockedPurchaseList)).thenReturn(true);
        doReturn(orderItems).when(orderItem1Service).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        doReturn(new ArrayList<ProductDto>()).when(productService).deduct(orderItems);

        OrderDto result = orderOrchestratorService.order(testUserId, mockedPurchaseList);

        // Assert
        assertEquals(result.id(), mockedOrderDto.id());
        verify(productService, times(1)).validate(mockedPurchaseList);
        verify(orderItem1Service, times(1)).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        verify(productService, times(1)).deduct(orderItems);
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
        Exception exception = assertThrows(BusinessException.class, () ->
                orderOrchestratorService.order(testUserId, mockedPurchaseList));
        assertEquals(BusinessException.class, exception.getClass());

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
        when(orderItem1Service.orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList)).thenThrow(new BusinessException("포인트 부족"));

        // Assert
        Exception exception = assertThrows(BusinessException.class, () ->
                orderOrchestratorService.order(testUserId, mockedPurchaseList));
        assertEquals(BusinessException.class, exception.getClass());

        verify(productService, times(1)).validate(mockedPurchaseList);
        verify(orderItem1Service, times(1)).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
    }

    @Test
    @DisplayName("order: 주문중 - 상품 재고 부족하여, 재고 처리 실패한 경우")
    public void order_whenNotPayable_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);
        List<OrderItem> orderItems = mockedOrderItemDtos.stream().map(OrderItem::toEntity).toList();

        when(userService.find(testUserId)).thenReturn(Optional.of(new User()));
        when(orderService.start(testUserId)).thenReturn(mockedOrder);
        when(productService.validate(mockedPurchaseList)).thenReturn(true);
        doReturn(orderItems).when(orderItem1Service).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        //
        when(productService.deduct(orderItems)).thenThrow(new BusinessException("재고 부족"));

        // Assert
        Exception exception = assertThrows(BusinessException.class, () ->
                orderOrchestratorService.order(testUserId, mockedPurchaseList));
        assertEquals(BusinessException.class, exception.getClass());

        verify(productService, times(1)).validate(mockedPurchaseList);
        verify(orderItem1Service, times(1)).orderEachProduct(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        verify(productService, times(1)).deduct(orderItems);
    }

    // # cancel
    @Test
    @DisplayName("cancel: 주문 취소")
    public void cancel() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);
        List<OrderItem> orderItems = mockedOrderItemDtos.stream().map(OrderItem::toEntity).toList();

        when(orderService.find(mockedOrderDto.id())).thenReturn(Optional.of(mockedOrder));
        when(orderItem1Service.cancelOrderItem(mockedOrderDto.id())).thenReturn(orderItems);
        doReturn(new ArrayList<ProductDto>()).when(productService).conduct(orderItems);

        OrderDto result = orderOrchestratorService.cancel(mockedOrderDto.id());

        //todo fix err
        assertEquals(result.id(), mockedOrderDto.id());
        assertEquals(result.state(), Result.CANCELED);
        verify(orderService, times(1)).find(mockedOrderDto.id());
        verify(orderItem1Service, times(1)).cancelOrderItem(mockedOrderDto.id());
        verify(productService, times(1)).conduct(orderItems);
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 주문 정보가 없는 경우")
    public void cancel_whenOrderNotFound_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);
        when(orderService.find(mockedOrderDto.id())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () ->
                orderOrchestratorService.cancel(mockedOrderDto.id()));
        assertEquals(BusinessException.class, exception.getClass());

        verify(orderService, times(1)).find(mockedOrderDto.id());
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 구매 취소 실패한 경우")
    public void cancel_whenPurchaseCancelFailed_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);

        when(orderService.find(mockedOrderDto.id())).thenReturn(Optional.of(mockedOrder));
        when(orderItem1Service.cancelOrderItem(mockedOrderDto.id())).thenThrow(new BusinessException("구매 취소 실패"));

        Exception exception = assertThrows(BusinessException.class, () ->
                orderOrchestratorService.cancel(mockedOrderDto.id()));
        assertEquals(BusinessException.class, exception.getClass());

        verify(orderService, times(1)).find(mockedOrderDto.id());
        verify(orderItem1Service, times(1)).cancelOrderItem(mockedOrderDto.id());
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 재고 취소 실패한 경우")
    public void cancel_whenUnstoreFailed_thenThrowsException() throws Exception {
        Order mockedOrder = Order.toEntity(mockedOrderDto);
        List<OrderItem> orderItems = mockedOrderItemDtos.stream().map(OrderItem::toEntity).toList();

        when(orderService.find(mockedOrderDto.id())).thenReturn(Optional.of(mockedOrder));
        when(orderItem1Service.cancelOrderItem(mockedOrderDto.id())).thenReturn(orderItems);
        when(productService.conduct(orderItems)).thenThrow(new BusinessException("재고 취소 실패"));

        Exception exception = assertThrows(BusinessException.class, () ->
                orderOrchestratorService.cancel(mockedOrderDto.id()));
        assertEquals(BusinessException.class, exception.getClass());

        verify(orderService, times(1)).find(mockedOrderDto.id());
        verify(orderItem1Service, times(1)).cancelOrderItem(mockedOrderDto.id());
        verify(productService, times(1)).conduct(orderItems);
    }
}

