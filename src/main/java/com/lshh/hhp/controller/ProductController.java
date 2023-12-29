package com.lshh.hhp.controller;

import com.lshh.hhp.biz.base.ProductBiz;
import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.ProductDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@AllArgsConstructor
@RestController
public class ProductController{
    final ProductBiz productService;

    @Operation(summary = "상품 전체", description = "상품 전체 리스트 확인")
    @GetMapping("/all")
    public ResultDto<List<ProductDto>> all(){
        return new ResultDto<>(productService.findAll());
    }
}