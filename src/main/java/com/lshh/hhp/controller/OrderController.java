package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.dto.OrderDto;
import com.lshh.hhp.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/order")
@AllArgsConstructor
@RestController
public class OrderController {

    final OrderService orderService;

    @PostMapping("/")
    public ResponseDto<Result> order(@RequestBody long userId, @RequestBody long productId) throws Exception {
        return new ResponseDto<>(orderService.order(userId, productId));
    }

    @GetMapping("/all/{userId}")
    public ResponseDto<List<OrderDto>> all(@PathVariable long userId){
        return new ResponseDto<>(orderService.findByUserId(userId));
    }
}
