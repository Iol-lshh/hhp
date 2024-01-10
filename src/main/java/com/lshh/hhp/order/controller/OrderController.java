package com.lshh.hhp.order.controller;

import com.lshh.hhp.common.ResultDto;
import com.lshh.hhp.order.dto.RequestPurchaseOrderDto;
import com.lshh.hhp.order.dto.OrderDto;
import com.lshh.hhp.order.service.OrderOrchestratorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/order")
@AllArgsConstructor
@RestController
public class OrderController {

    final OrderOrchestratorService orderService;

    @Operation(summary = "주문 - 동기 처리 (강결합)")
    @PostMapping("/purchase")
    public ResultDto<OrderDto> order(@RequestBody RequestPurchaseOrderDto dto) throws Exception {
        return new ResultDto<>(orderService.order(dto.getUserId(), dto.getRequestPurchaseList()));
    }

    @Operation(summary = "주문 내역")
    @GetMapping("/all/{userId}")
    public ResultDto<List<OrderDto>> all(@PathVariable Long userId){
        return new ResultDto<>(orderService.findByUserId(userId));
    }
}
