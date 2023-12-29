package com.lshh.hhp.controller;

import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.biz.biz1.PurchaseBiz1;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/purchase")
@AllArgsConstructor
@RestController
public class PurchaseController {
    final PurchaseBiz1 purchaseService;

    @Operation(summary = "전체 기간 인기 판매 상품")
    @GetMapping("/favorite/{count}")
    public ResultDto<List<ViewPurchasedProductDto>> favorite(@PathVariable Integer count){
        return new ResultDto<>(purchaseService.favorite(count));
    }
}
