package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.dto.ProductDto;
import com.lshh.hhp.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/product")
@AllArgsConstructor
@RestController
public class ProductController{
    final ProductService productService;

    // 상품 리스트 확인
    @GetMapping("/all")
    public ResponseDto<List<ProductDto>> all(){
        return new ResponseDto<>(productService.findAll());
    }

    // 상품 등록
}