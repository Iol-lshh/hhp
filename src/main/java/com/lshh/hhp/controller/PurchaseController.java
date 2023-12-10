package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/purchase")
@AllArgsConstructor
@RestController
public class PurchaseController {
    final PurchaseService purchaseService;

    @PostMapping()
    public ResponseDto<Result> purchase(@RequestBody long userId, @RequestBody int paid) throws Exception {
        return new ResponseDto<>(purchaseService.purchase(userId, paid));
    }
}
