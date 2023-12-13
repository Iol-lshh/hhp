package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.dto.RequestInputDto;
import com.lshh.hhp.dto.StockDto;
import com.lshh.hhp.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/stock")
@AllArgsConstructor
@RestController
public class StockController {
    final StockService stockService;

    @Operation(summary = "stock 추가")
    @PostMapping("/input")
    public ResponseDto<List<StockDto>> input(@RequestBody RequestInputDto dto){
        return stockService.input(dto.getProductId(), dto.getCnt()).toResponseDto();
    }

    @Operation(summary = "stock 확인")
    @GetMapping("/all")
    public ResponseDto<List<StockDto>> all(){
        return new ResponseDto<>(stockService.findAll());
    }
}
