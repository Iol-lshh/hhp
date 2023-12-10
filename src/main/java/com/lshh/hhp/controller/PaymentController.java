package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.Response.*;
import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pay")
@AllArgsConstructor
@RestController
public class PaymentController {

    final PaymentService paymentService;
    @PostMapping("/exchange")
    public ResponseDto<Result> exchange(@RequestBody long userId, @RequestBody int toNeed) throws Exception {
        return new ResponseDto<>(paymentService.exchange(userId, toNeed));
    }
}
