package com.lshh.hhp.orderItem;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.orderItem.service.OrderItemService;
import com.lshh.hhp.orderItem.service.OrderItem1ServiceImpl;
import com.lshh.hhp.point.service.PointService;
import com.lshh.hhp.product.Product;
import com.lshh.hhp.product.dto.ProductDto;
import com.lshh.hhp.product.service.ProductService;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import org.assertj.core.api.AssertionsForClassTypes;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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

    @Test
    public void testFavorite() {
        // Preparing mock favorite list
        List<Product> favoriteList = IntStream.range(0, 5)
                .mapToObj(i -> Product.toEntity(
                        new ProductDto()
                                .id((long) i)
                                .name("Product " + i)
                                .price(i * 10)
                                .stockCnt(10)
                )).collect(Collectors.toList());

        // Mock ProductService's favorite behavior
        when(productService.favorite(5)).thenReturn(favoriteList);

        List<ProductDto> favoriteResult = orderItem1ServiceImpl.favorite(5);

        assertThat(favoriteResult).hasSize(5);
        AssertionsForClassTypes.assertThat(favoriteResult.get(0).id()).isEqualTo(0L);
        AssertionsForClassTypes.assertThat(favoriteResult.get(0).name()).isEqualTo("Product 0");
        AssertionsForClassTypes.assertThat(favoriteResult.get(0).price()).isEqualTo(0);
    }
}