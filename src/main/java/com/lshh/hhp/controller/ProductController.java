package com.lshh.hhp.controller;

import com.lshh.hhp.dto.ResponseDto;
import com.lshh.hhp.dto.origin.ProductDto;
import com.lshh.hhp.biz.biz1.ProductBiz1;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@AllArgsConstructor
@RestController
public class ProductController{
    final ProductBiz1 productService;

    @Operation(summary = "상품 전체", description = "상품 전체 리스트 확인")
    @GetMapping("/all")
    public ResponseDto<List<ProductDto>> all(){
        return new ResponseDto<>(productService.findAll());
    }
}