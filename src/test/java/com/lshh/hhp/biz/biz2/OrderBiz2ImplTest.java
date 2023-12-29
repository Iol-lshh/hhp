package com.lshh.hhp.biz.biz2;

import com.lshh.hhp.biz.base.OrderBiz;
import com.lshh.hhp.biz.base.PointBiz;
import com.lshh.hhp.biz.base.ProductBiz;
import com.lshh.hhp.biz.base.UserBiz;
import com.lshh.hhp.biz.biz1.PurchaseBiz1;
import com.lshh.hhp.dto.Response.Result;
import com.lshh.hhp.dto.origin.OrderDto;
import com.lshh.hhp.dto.origin.ProductDto;
import com.lshh.hhp.dto.origin.PurchaseDto;
import com.lshh.hhp.dto.origin.UserDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderBiz2ImplTest {

    @InjectMocks
    private OrderBiz2Impl orderService;
    @Mock
    private OrderBiz orderComponent;

    @Mock
    private PurchaseBiz1 purchaseComponent;
    @Mock
    private UserBiz userComponent;
    @Mock
    private PointBiz pointComponent;
    @Mock
    private ProductBiz productComponent;


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
        assertEquals(expectedOrderDtos, result);
    }


    // # order
    @Test
    @DisplayName("order: 주문 가능한 경우")
    public void order() throws Exception {
        when(userComponent.find(testUserId)).thenReturn(Optional.of(new UserDto()));
        when(orderComponent.start(testUserId)).thenReturn(mockedOrderDto);
        when(productComponent.validate(mockedPurchaseList)).thenReturn(true);
        doReturn(new ArrayList<ProductDto>()).when(productComponent).unstore(mockedPurchaseList);
        doReturn(Arrays.asList(mockedPurchasedDto)).when(purchaseComponent).purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        when(orderComponent.success(mockedOrderDto)).thenReturn(mockedOrderDto);

        OrderDto result = orderService.order(testUserId, mockedPurchaseList);

        // Assert
        assertEquals(result, mockedOrderDto);
        verify(productComponent, times(1)).validate(mockedPurchaseList);
        verify(productComponent, times(1)).unstore(mockedPurchaseList);
        verify(purchaseComponent, times(1)).purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList);
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
    @DisplayName("order: 주문중 - 상품 재고 부족하여, 재고 처리 실패한 경우")
    public void order_whenNotPayable_thenThrowsException() throws Exception {
        when(userComponent.find(testUserId)).thenReturn(Optional.of(new UserDto()));
        when(orderComponent.start(testUserId)).thenReturn(mockedOrderDto);
        when(productComponent.validate(mockedPurchaseList)).thenReturn(true);
        //
        when(productComponent.unstore(mockedPurchaseList)).thenThrow(new Exception("재고 부족"));

        // Assert
        Exception exception = assertThrows(Exception.class, () ->
                orderService.order(testUserId, mockedPurchaseList));
        assertEquals("재고 부족", exception.getMessage());

        verify(productComponent, times(1)).validate(mockedPurchaseList);
        verify(productComponent, times(1)).unstore(mockedPurchaseList);
        verify(purchaseComponent, times(0)).purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList);
        verify(orderComponent, times(1)).fail(mockedOrderDto);
        verify(orderComponent, times(0)).success(mockedOrderDto);
    }

    @Test
    @DisplayName("order: 주문중 - 지불 불가하여, 구매 처리 실패한 경우")
    public void order_whenProductOutOfStock_thenThrowsException() throws Exception {
        when(userComponent.find(testUserId)).thenReturn(Optional.of(new UserDto()));
        when(orderComponent.start(testUserId)).thenReturn(mockedOrderDto);
        when(productComponent.validate(mockedPurchaseList)).thenReturn(true);
        doReturn(new ArrayList<ProductDto>()).when(productComponent).unstore(mockedPurchaseList);
        //
        when(purchaseComponent.purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList)).thenThrow(new Exception("포인트 부족"));

        // Assert
        Exception exception = assertThrows(Exception.class, () ->
                orderService.order(testUserId, mockedPurchaseList));
        assertEquals("포인트 부족", exception.getMessage());

        verify(productComponent, times(1)).validate(mockedPurchaseList);
        verify(productComponent, times(1)).unstore(mockedPurchaseList);
        verify(purchaseComponent, times(1)).purchase(testUserId, mockedOrderDto.id(), mockedPurchaseList);
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
        doReturn(new ArrayList<ProductDto>()).when(productComponent).restore(mockedPurchasedList);
        when(orderComponent.finishedCancel(mockedOrderDto)).thenReturn(mockedOrderDto.state(Result.CANCELED));

        OrderDto result = orderService.cancel(mockedOrderDto.id());

        assertEquals(mockedOrderDto.state(Result.CANCELED), result);
        verify(orderComponent, times(1)).find(mockedOrderDto.id());
        verify(orderComponent, times(1)).startCancel(mockedOrderDto);
        verify(purchaseComponent, times(1)).cancel(mockedOrderDto.id());
        verify(productComponent, times(1)).restore(mockedPurchasedList);
        verify(orderComponent, times(1)).finishedCancel(mockedOrderDto);
    }
    @Test
    @DisplayName("cancel: 주문 취소 - 주문 정보가 없는 경우")
    public void cancel_whenOrderNotFound_thenThrowsException() {
        when(orderComponent.find(mockedOrderDto.id())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () ->
                orderService.cancel(mockedOrderDto.id()));
        assertEquals("주문 정보를 찾을 수 없습니다.", exception.getMessage());

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
        when(productComponent.restore(mockedPurchasedList)).thenThrow(new Exception("재고 취소 실패"));

        Exception exception = assertThrows(Exception.class, () ->
                orderService.cancel(mockedOrderDto.id()));
        assertEquals("재고 취소 실패", exception.getMessage());

        verify(orderComponent, times(1)).find(mockedOrderDto.id());
        verify(orderComponent, times(1)).startCancel(mockedOrderDto);
        verify(purchaseComponent, times(1)).cancel(mockedOrderDto.id());
        verify(productComponent, times(1)).restore(mockedPurchasedList);
        verify(orderComponent, times(0)).finishedCancel(mockedOrderDto);
    }

}

