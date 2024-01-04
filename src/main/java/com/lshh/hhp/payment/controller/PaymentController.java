package com.lshh.hhp.payment.controller;

import com.lshh.hhp.common.ResultDto;
import com.lshh.hhp.dto.request.RequestExchangeDto;
import com.lshh.hhp.payment.dto.PaymentDto;
import com.lshh.hhp.payment.service.Payment1Service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pay")
@AllArgsConstructor
@RestController
public class PaymentController {

    final Payment1Service paymentService;
    
    @Operation(summary = "포인트 생성")
    @PostMapping("/exchange")
    public ResultDto<PaymentDto> exchange(@RequestBody RequestExchangeDto request) throws Exception {
        return new ResultDto<>(paymentService.exchange(request.getUserId(), request.getToNeed()));
    }
}
