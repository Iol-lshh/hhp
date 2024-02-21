package com.lshh.hhp.controller.payment;

import com.lshh.hhp.common.ResultDto;
import com.lshh.hhp.domain.payment.Payment;
import com.lshh.hhp.controller.payment.request.RequestExchangeDto;
import com.lshh.hhp.domain.payment.dto.PaymentDto;
import com.lshh.hhp.domain.payment.Payment1Service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/pay")
@AllArgsConstructor
@RestController
public class PaymentController {

    final Payment1Service paymentService;
    
    @Operation(summary = "포인트 생성")
    @PostMapping("/exchange")
    public ResultDto<PaymentDto> exchange(@RequestBody RequestExchangeDto request) throws Exception {
        return ResultDto.ok(paymentService.exchange(request.getUserId(), request.getToNeed()));
    }

    @Operation(summary = "히스토리")
    @GetMapping("/history/{userId}")
    public ResultDto<List<PaymentDto>> findByUserId(@PathVariable Long userId){
        return ResultDto.ok(paymentService.findByUserId(userId).stream().map(Payment::toDto).toList());
    }
}