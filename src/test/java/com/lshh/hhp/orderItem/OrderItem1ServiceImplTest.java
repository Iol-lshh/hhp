package com.lshh.hhp.orderItem;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.orderItem.service.OrderItemService;
import com.lshh.hhp.orderItem.service.OrderItem1ServiceImpl;
import com.lshh.hhp.point.service.PointService;
import com.lshh.hhp.product.service.ProductService;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderItem1ServiceImplTest {

    @Mock
    PointService pointService;
    @Mock
    OrderItemService orderItemServiceImpl;
    @Mock
    ProductService productService;

    @InjectMocks
    OrderItem1ServiceImpl orderItem1ServiceImpl;

    OrderItemDto orderItemDto;
    RequestPurchaseDto requestPurchaseDto;
    List<RequestPurchaseDto> requestPurchaseList;

    @BeforeEach
    public void setup() {
        orderItemDto = new OrderItemDto()
                .userId(1L)
                .orderId(1L)
                .productId(1L)
                .toPay(100)
                .count(1)
                .state(Response.Result.OK);

        requestPurchaseDto = new RequestPurchaseDto()
                .setProductId(orderItemDto.productId())
                .setCount(orderItemDto.count());

        this.requestPurchaseList = Arrays.asList(this.requestPurchaseDto);
    }

    @Test
    @DisplayName("구매 성공")
    void purchase() throws Exception {
        OrderItem orderItem = OrderItem.toEntity(orderItemDto);
        when(orderItemServiceImpl.save(any())).thenReturn(Arrays.asList(orderItem));

        // Act
        List<OrderItemDto> result = orderItem1ServiceImpl.orderEachProduct(orderItemDto.userId(), orderItemDto.orderId(), Collections.singletonList(requestPurchaseDto));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(orderItemServiceImpl, times(1)).save(any());
        verify(pointService, times(1)).subtract(any());
      }
}