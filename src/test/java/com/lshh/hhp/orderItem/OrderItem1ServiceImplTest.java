package com.lshh.hhp.orderItem;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.domain.order.item.OrderItem;
import com.lshh.hhp.domain.order.item.OrderItem1Service;
import com.lshh.hhp.domain.order.item.dto.OrderItemDto;
import com.lshh.hhp.domain.order.item.OrderItemRepository;
import com.lshh.hhp.domain.product.Product;
import com.lshh.hhp.domain.product.dto.ProductDto;
import com.lshh.hhp.domain.product.ProductService;
import com.lshh.hhp.domain.product.dto.RequestProductDto;
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
    OrderItemRepository orderItemRepository;
    @Mock
    ProductService productService;

    @InjectMocks
    OrderItem1Service orderItem1Service;

    OrderItemDto orderItemDto;
    RequestProductDto requestPurchaseDto;
    List<RequestProductDto> requestPurchaseList;

    @BeforeEach
    public void setup() {
        orderItemDto = new OrderItemDto()
                .userId(1L)
                .orderId(1L)
                .productId(1L)
                .toPay(100)
                .count(1)
                .state(Response.Result.OK);

        requestPurchaseDto = new RequestProductDto()
                .setProductId(orderItemDto.productId())
                .setCount(orderItemDto.count());

        this.requestPurchaseList = Arrays.asList(this.requestPurchaseDto);
    }

    @Test
    @DisplayName("구매 성공")
    void purchase() throws Exception {
        OrderItem orderItem = OrderItem.toEntity(orderItemDto);
        when(orderItemRepository.saveAllAndFlush(any())).thenReturn(Arrays.asList(orderItem));

        // Act
        List<OrderItem> result = orderItem1Service.orderEachProduct(orderItemDto.userId(), orderItemDto.orderId(), Collections.singletonList(requestPurchaseDto));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
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

        List<ProductDto> favoriteResult = orderItem1Service.favorite(5);

        assertThat(favoriteResult).hasSize(5);
        AssertionsForClassTypes.assertThat(favoriteResult.get(0).id()).isEqualTo(0L);
        AssertionsForClassTypes.assertThat(favoriteResult.get(0).name()).isEqualTo("Product 0");
        AssertionsForClassTypes.assertThat(favoriteResult.get(0).price()).isEqualTo(0);
    }
}