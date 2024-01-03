package com.lshh.hhp.order;

import com.lshh.hhp.product.ProductBase;
import com.lshh.hhp.user.UserBase;
import com.lshh.hhp.purchase.PurchaseService;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.product.ProductDto;
import com.lshh.hhp.purchase.PurchaseDto;
import com.lshh.hhp.user.UserDto;
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
    private OrderOrchestratorImpl orderService;
    @Mock
    private OrderBase orderComponent;

    @Mock
    private PurchaseService purchaseComponent;
    @Mock
    private UserBase userComponent;
    @Mock
    private ProductBase productComponent;

    @Mock
    private ApplicationEventPublisher publisher;


    long testUserId = 1L;
    RequestPurchaseDto mockedPurchaseDto;
    OrderDto mockedOrderDto;
    List<RequestPurchaseDto> mockedPurchaseList;
    PurchaseDto mockedPurchasedDto;
    List<PurchaseDto> mockedPurchasedList;

    @BeforeEach
    public void setup() {
        this.mockedPurchaseDto = new RequestPurchaseDto().setCount(1);
        this.mockedOrderDto = new OrderDto(1L, this.testUserId, Result.START);
        this.mockedPurchaseList = Arrays.asList(this.mockedPurchaseDto);
        this.mockedPurchasedDto = new PurchaseDto()
                .id(1L)
                .orderId(mockedOrderDto.id())
                .userId(testUserId)
                .count(mockedPurchaseDto.getCount())
                .productId(mockedPurchaseDto.getProductId())
                .paid(11);
        this.mockedPurchasedList = Arrays.asList(mockedPurchasedDto);
    }

    // findByUserId
    @Test
    @DisplayName("유저 주문 정보 조회")
    public void findByUserId() {

        List<OrderDto> expectedOrderDtos = new ArrayList<>();
        expectedOrderDtos.add(mockedOrderDto);

        when(orderComponent.findByUserId(Mockito.anyLong())).thenReturn(expectedOrderDtos);

        // Act
        List<OrderDto> result = orderService.findByUserId(testUserId);

        // Assert
        assert expectedOrderDtos.equals(result);
    }


    // # order
    @Test
    @DisplayName("order: 주문 가능한 경우")
    public void order() throws Exception {
        when(userComponent.find(testUserId)).thenReturn(Optional.of(new UserDto()));
        when(orderComponent.start(testUserId)).thenReturn(mockedOrderDto);
        when(productComponent.validate(mockedPurchaseList)).thenReturn(true);
        doReturn(Arrays.asList(mockedPurchasedDto)).when(purchaseComponent).purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        doReturn(new ArrayList<ProductDto>()).when(productComponent).deduct(mockedPurchaseList);
        when(orderComponent.success(mockedOrderDto)).thenReturn(mockedOrderDto);

        OrderDto result = orderService.order(testUserId, mockedPurchaseList);

        // Assert
        assertEquals(result, mockedOrderDto);
        verify(productComponent, times(1)).validate(mockedPurchaseList);
        verify(purchaseComponent, times(1)).purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        verify(productComponent, times(1)).deduct(mockedPurchaseList);
        verify(orderComponent, times(0)).fail(mockedOrderDto);
        verify(orderComponent, times(1)).success(mockedOrderDto);
    }

    @Test
    @DisplayName("order: 주문중 - 상품 조회 불가한 경우")
    public void order_whenProductNotValid_thenThrowsException() {
        when(userComponent.find(testUserId)).thenReturn(Optional.of(new UserDto()));
        when(orderComponent.start(testUserId)).thenReturn(mockedOrderDto);
        //
        when(productComponent.validate(mockedPurchaseList)).thenReturn(false);

        // Assert
        Exception exception = assertThrows(Exception.class, () ->
                orderService.order(testUserId, mockedPurchaseList));
        assertEquals("잘못된 상품", exception.getMessage());

        verify(productComponent, times(1)).validate(mockedPurchaseList);
        verify(orderComponent, times(1)).fail(mockedOrderDto);
        verify(orderComponent, times(0)).success(mockedOrderDto);
    }

    @Test
    @DisplayName("order: 주문중 - 지불 불가하여, 구매 처리 실패한 경우")
    public void order_whenProductOutOfStock_thenThrowsException() throws Exception {
        when(userComponent.find(testUserId)).thenReturn(Optional.of(new UserDto()));
        when(orderComponent.start(testUserId)).thenReturn(mockedOrderDto);
        when(productComponent.validate(mockedPurchaseList)).thenReturn(true);
        //
        when(purchaseComponent.purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList)).thenThrow(new Exception("포인트 부족"));

        // Assert
        Exception exception = assertThrows(Exception.class, () ->
                orderService.order(testUserId, mockedPurchaseList));
        assertEquals("포인트 부족", exception.getMessage());

        verify(productComponent, times(1)).validate(mockedPurchaseList);
        verify(purchaseComponent, times(1)).purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        verify(orderComponent, times(1)).fail(mockedOrderDto);
        verify(orderComponent, times(0)).success(mockedOrderDto);
    }

    @Test
    @DisplayName("order: 주문중 - 상품 재고 부족하여, 재고 처리 실패한 경우")
    public void order_whenNotPayable_thenThrowsException() throws Exception {
        when(userComponent.find(testUserId)).thenReturn(Optional.of(new UserDto()));
        when(orderComponent.start(testUserId)).thenReturn(mockedOrderDto);
        when(productComponent.validate(mockedPurchaseList)).thenReturn(true);
        doReturn(Arrays.asList(mockedPurchasedDto)).when(purchaseComponent).purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        //
        when(productComponent.deduct(mockedPurchaseList)).thenThrow(new Exception("재고 부족"));

        // Assert
        Exception exception = assertThrows(Exception.class, () ->
                orderService.order(testUserId, mockedPurchaseList));
        assertEquals("재고 부족", exception.getMessage());

        verify(productComponent, times(1)).validate(mockedPurchaseList);
        verify(purchaseComponent, times(1)).purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        verify(productComponent, times(1)).deduct(mockedPurchaseList);
        verify(orderComponent, times(1)).fail(mockedOrderDto);
        verify(orderComponent, times(0)).success(mockedOrderDto);
    }

    // # cancel
    @Test
    @DisplayName("cancel: 주문 취소")
    public void cancel() throws Exception {

        when(orderComponent.find(mockedOrderDto.id())).thenReturn(Optional.of(mockedOrderDto));
        when(orderComponent.startCancel(mockedOrderDto)).thenReturn(mockedOrderDto.state(Result.CANCELING));
        when(purchaseComponent.cancel(mockedOrderDto.id())).thenReturn(mockedPurchasedList);
        doReturn(new ArrayList<ProductDto>()).when(productComponent).conduct(mockedPurchasedList);
        when(orderComponent.finishedCancel(mockedOrderDto)).thenReturn(mockedOrderDto.state(Result.CANCELED));

        OrderDto result = orderService.cancel(mockedOrderDto.id());

        assertEquals(mockedOrderDto.state(Result.CANCELED), result);
        verify(orderComponent, times(1)).find(mockedOrderDto.id());
        verify(orderComponent, times(1)).startCancel(mockedOrderDto);
        verify(purchaseComponent, times(1)).cancel(mockedOrderDto.id());
        verify(productComponent, times(1)).conduct(mockedPurchasedList);
        verify(orderComponent, times(1)).finishedCancel(mockedOrderDto);
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 주문 정보가 없는 경우")
    public void cancel_whenOrderNotFound_thenThrowsException() {
        when(orderComponent.find(mockedOrderDto.id())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () ->
                orderService.cancel(mockedOrderDto.id()));
        assertEquals("잘못된 주문", exception.getMessage());

        verify(orderComponent, times(1)).find(mockedOrderDto.id());
        verify(orderComponent, times(0)).startCancel(mockedOrderDto);
        verify(orderComponent, times(0)).finishedCancel(mockedOrderDto);
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 구매 취소 실패한 경우")
    public void cancel_whenPurchaseCancelFailed_thenThrowsException() throws Exception {
        when(orderComponent.find(mockedOrderDto.id())).thenReturn(Optional.of(mockedOrderDto));
        when(orderComponent.startCancel(mockedOrderDto)).thenReturn(mockedOrderDto.state(Result.CANCELING));
        when(purchaseComponent.cancel(mockedOrderDto.id())).thenThrow(new Exception("구매 취소 실패"));

        Exception exception = assertThrows(Exception.class, () ->
                orderService.cancel(mockedOrderDto.id()));
        assertEquals("구매 취소 실패", exception.getMessage());

        verify(orderComponent, times(1)).find(mockedOrderDto.id());
        verify(orderComponent, times(1)).startCancel(mockedOrderDto);
        verify(purchaseComponent, times(1)).cancel(mockedOrderDto.id());
        verify(orderComponent, times(0)).finishedCancel(mockedOrderDto);
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 재고 취소 실패한 경우")
    public void cancel_whenUnstoreFailed_thenThrowsException() throws Exception {
        when(orderComponent.find(mockedOrderDto.id())).thenReturn(Optional.of(mockedOrderDto));
        when(orderComponent.startCancel(mockedOrderDto)).thenReturn(mockedOrderDto.state(Result.CANCELING));
        when(purchaseComponent.cancel(mockedOrderDto.id())).thenReturn(mockedPurchasedList);
        when(productComponent.conduct(mockedPurchasedList)).thenThrow(new Exception("재고 취소 실패"));

        Exception exception = assertThrows(Exception.class, () ->
                orderService.cancel(mockedOrderDto.id()));
        assertEquals("재고 취소 실패", exception.getMessage());

        verify(orderComponent, times(1)).find(mockedOrderDto.id());
        verify(orderComponent, times(1)).startCancel(mockedOrderDto);
        verify(purchaseComponent, times(1)).cancel(mockedOrderDto.id());
        verify(productComponent, times(1)).conduct(mockedPurchasedList);
        verify(orderComponent, times(0)).finishedCancel(mockedOrderDto);
    }

}
