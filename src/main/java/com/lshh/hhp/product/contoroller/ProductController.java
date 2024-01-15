package com.lshh.hhp.product.contoroller;

import com.lshh.hhp.common.ResultDto;
import com.lshh.hhp.product.Product;
import com.lshh.hhp.product.dto.ProductDto;
import com.lshh.hhp.product.dto.RequestProductSetDto;
import com.lshh.hhp.product.service.ProductService;
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
    public ResultDto<List<ProductDto>> all(){
        return ResultDto.ok(productService.findAll().stream().map(Product::toDto).toList());
    }

    @Operation(summary = "상품 재고 추가", description = "상품 재고 추가")
    @PostMapping("/conduct")
    public ResultDto<Integer> conduct(@RequestBody RequestProductSetDto productSetDto){
        productService.conductRequest(productSetDto.getProductDtoList());
        return ResultDto.ok();
    }
}