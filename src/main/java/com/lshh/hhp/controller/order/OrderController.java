package com.lshh.hhp.controller.order;

import com.lshh.hhp.common.ResultDto;
import com.lshh.hhp.domain.order.OrderOrchestratorService;
import com.lshh.hhp.domain.order.dto.OrderDetailDto;
import com.lshh.hhp.controller.product.request.RequestProductSetDto;
import com.lshh.hhp.domain.order.dto.OrderDto;
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
    public ResultDto<OrderDto> order(@RequestBody RequestProductSetDto dto) throws Exception {
        return ResultDto.ok(orderService.order(dto.getUserId(), dto.getProductDtoList()));
    }

    @Operation(summary = "주문 내역")
    @GetMapping("/all/{userId}")
    public ResultDto<List<OrderDto>> all(@PathVariable Long userId){
        return ResultDto.ok(orderService.findByUserId(userId));
    }

    @Operation(summary = "주문 실패 내역")
    @GetMapping("/failed/{userId}")
    public ResultDto<List<OrderDto>> failed(@PathVariable Long userId){
        return ResultDto.ok(orderService.findFailedByUserId(userId));
    }

    @Operation(summary = "주문 상세 내역")
    @GetMapping("/detail/{orderId}")
    public ResultDto<OrderDetailDto> findDetail(@PathVariable Long orderId){
        return ResultDto.ok(orderService.findDetail(orderId));
    }
}
