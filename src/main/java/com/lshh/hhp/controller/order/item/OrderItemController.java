package com.lshh.hhp.controller.order.item;

import com.lshh.hhp.common.ResultDto;
import com.lshh.hhp.domain.order.item.OrderItem1Service;
import com.lshh.hhp.domain.product.dto.ProductDto;
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
public class OrderItemController {
    final OrderItem1Service purchaseService;

    @Operation(summary = "전체 기간 인기 판매 상품")
    @GetMapping("/favorite/{count}")
    public ResultDto<List<ProductDto>> favorite(@PathVariable Integer count){
        return ResultDto.ok(purchaseService.favorite(count));
    }
}
