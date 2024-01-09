package com.lshh.hhp.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lshh.hhp.order.service.OrderOrchestratorService;
import com.lshh.hhp.order.controller.OrderController;
import com.lshh.hhp.common.Response;
import com.lshh.hhp.common.ResultDto;
import com.lshh.hhp.order.dto.OrderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderOrchestratorService orderService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    @DisplayName("주문")
    public void order() throws Exception {
        // given
        OrderDto expectedOrderDto = new OrderDto().id(1L).userId(1L).state(Response.Result.SUCCESS);
        when(orderService.order(anyLong(), any())).thenReturn(expectedOrderDto);

        mockMvc.perform(post("/order/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"requestPurchaseList\":[]}"))
                .andExpect(status().isOk());
        verify(orderService, times(1)).order(anyLong(), any());
    }

    @Test
    @DisplayName("주문 내역 조회")
    public void findOrder() throws Exception {
        // Given
        OrderDto order1 = new OrderDto().id(1L).state( Response.Result.SUCCESS).userId(1L);
        OrderDto order2 = new OrderDto().id(2L).state( Response.Result.SUCCESS).userId(2L);
        List<OrderDto> mockOrderList = Arrays.asList(order1, order2);
        when(orderService.findByUserId(anyLong())).thenReturn(mockOrderList);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/order/all/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseJson = mvcResult.getResponse().getContentAsString();
        JavaType orderDtoJavaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, OrderDto.class);
        JavaType resultDtoJavaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(ResultDto.class, orderDtoJavaType);
        ResultDto<List<OrderDto>> result = OBJECT_MAPPER.readValue(responseJson, resultDtoJavaType);
        assertEquals(result.getValue().size(), 2);
        assertEquals(result.getValue().get(0).id(), 1L);
        assertEquals(result.getValue().get(1).id(), 2L);
    }
}