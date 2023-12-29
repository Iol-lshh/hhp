package com.lshh.hhp.controller;

import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.OrderDto;
import com.lshh.hhp.dto.request.RequestPurchaseOrderDto;
import com.lshh.hhp.biz.biz2.OrderBiz2;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/order")
@AllArgsConstructor
@RestController
public class OrderController {

    final OrderBiz2 orderService;

    @Operation(summary = "주문 - 동기 처리 (강결합)")
    @PostMapping("/purchase")
    public ResultDto<OrderDto> order(@RequestBody RequestPurchaseOrderDto dto) throws Exception {
        return new ResultDto<>(orderService.order(dto.getUserId(), dto.getRequestPurchaseList()));
    }

    @Operation(summary = "주문 시도 - 이벤트 발행 처리 (약결합)")
    @PostMapping("/tryPurchase")
    public ResultDto<OrderDto> tryOrder(@RequestBody RequestPurchaseOrderDto dto) throws Exception {
        return new ResultDto<>(orderService.start(dto.getUserId(), dto.getRequestPurchaseList()));
    }

    @Operation(summary = "주문 내역")
    @GetMapping("/all/{userId}")
    public ResultDto<List<OrderDto>> all(@PathVariable Long userId){
        return new ResultDto<>(orderService.findByUserId(userId));
    }
}
