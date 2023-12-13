package com.lshh.hhp.controller;

import com.lshh.hhp.common.dto.ResponseDto;
import com.lshh.hhp.dto.RequestExchangeDto;
import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.service.PaymentService;
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

    final PaymentService paymentService;
    
    @Operation(summary = "포인트 생성")
    @PostMapping("/exchange")
    public ResponseDto<PaymentDto> exchange(@RequestBody RequestExchangeDto dto) throws Exception {
        return paymentService.exchange(dto.getUserId(), dto.getToNeed()).toResponseDto();
    }
}
