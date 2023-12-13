package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.dto.OrderDto;
import com.lshh.hhp.dto.RequestPurchaseOrderDto;
import com.lshh.hhp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/order")
@AllArgsConstructor
@RestController
public class OrderController {

    final OrderService orderService;

    @Operation(summary = "주문")
    @PostMapping("/purchase")
    public ResponseDto<OrderDto> order(@RequestBody RequestPurchaseOrderDto dto) throws Exception {
        return orderService.order(dto.getUserId(), dto.getRequestPurchaseList()).toResponseDto();
    }

    @Operation(summary = "주문 내역")
    @GetMapping("/all/{userId}")
    public ResponseDto<List<OrderDto>> all(@PathVariable Long userId){
        return new ResponseDto<>(orderService.findByUserId(userId));
    }
}
