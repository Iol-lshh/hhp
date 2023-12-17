package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.service.component.PaymentComponent;

import java.util.List;
import java.util.Optional;

public interface PaymentService extends PaymentComponent {
    ResultDto<PaymentDto> exchange(long userId, int toNeed) throws Exception;
}
