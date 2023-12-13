package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.dto.ProductDto;
import com.lshh.hhp.dto.UserDto;
import com.lshh.hhp.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@AllArgsConstructor
@RestController
public class ProductController{
    final ProductService productService;

    @Operation(summary = "상품 전체", description = "상품 전체 리스트 확인")
    @GetMapping("/all")
    public ResponseDto<List<ProductDto>> all(){
        return new ResponseDto<>(productService.findAll());
    }

    // 상품 등록
    @Operation(summary = "Product 생성, 갱신")
    @PostMapping("/")
    public ResponseDto<ProductDto> save(@RequestBody ProductDto productDto) throws Exception {
        return productService.save(productDto).toResponseDto();
    }
}