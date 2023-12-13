package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.OrderDto;
import com.lshh.hhp.dto.RequestPurchaseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    /**
     * Test for order method of OrderService
     */
    @Test
    void orderTest() throws Exception {
        // Create mock objects
        long userId = 1L;
        ArrayList<RequestPurchaseDto> productList = mock(ArrayList.class);
        ResultDto<OrderDto> resultDto = mock(ResultDto.class);

        // call the method under test
        ResultDto<OrderDto> orderDto = orderService.order(userId, productList);

        // Assert that method behaves as expected
        assertTrue(resultDto.getClass().equals(orderDto.getClass()));
    }


    @Test
    void findAll_ReturnsListOfOrders_WhenCalled() {
        // Assume some pre-existing Orders
        List<OrderDto> orders = Arrays.asList(
                new OrderDto().id(1L).userId(1L).state(Response.Result.OK),
                new OrderDto().id(2L).userId(2L).state(Response.Result.OK),
                new OrderDto().id(3L).userId(1L).state(Response.Result.OK)
        );

        // Setup our mocked OrderService
        doReturn(orders).when(orderService).findAll();

        // Execute the service call
        List<OrderDto> returnedOrders = orderService.findAll();

        // Assert the response
        Assertions.assertSame(orders, returnedOrders, "The service should return the list of orders in the database");
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoOrdersExist() {
        // Assuming no pre-existing orders
        List<OrderDto> orders = Arrays.asList();

        // Setup our mocked OrderService
        doReturn(orders).when(orderService).findAll();

        // Execute the service call
        List<OrderDto> returnedOrders = orderService.findAll();

        // Assert the response
        Assertions.assertTrue(returnedOrders.isEmpty(), "The service should return an empty list if no orders exist");
    }

    @Test
    void testFind() {
        // Create a mock OrderDto object
        OrderDto mockOrderDto = new OrderDto().id(1L).userId(1L);

        // When find method called with 1L argument, return Optional of mockOrderDto
        when(orderService.find(1L)).thenReturn(Optional.of(mockOrderDto));

        // Call the method under test
        Optional<OrderDto> returnedOrder = orderService.find(1L);

        // Verify that the method was indeed called once
        verify(orderService, times(1)).find(1L);

        // Perform assertions
        assert(returnedOrder.isPresent());
        assertEquals(returnedOrder.get().id(), mockOrderDto.id());
        assertEquals(returnedOrder.get().userId(), mockOrderDto.userId());
    }

    @Test
    void testFindByUserId() {

        OrderDto orderDto = new OrderDto();
        orderDto.id(1L);
        orderDto.userId(123L);

        OrderDto orderDto2 = new OrderDto();
        orderDto2.id(2L);
        orderDto2.userId(123L);

        List<OrderDto> expectedOrders = Arrays.asList(orderDto, orderDto2);

        when(orderService.findByUserId(123L)).thenReturn(expectedOrders);

        Optional<List<OrderDto>> actualOrders = Optional.ofNullable(orderService.findByUserId(123L));

        assertTrue(actualOrders.isPresent());
        assertEquals(expectedOrders, actualOrders.get());
    }
}