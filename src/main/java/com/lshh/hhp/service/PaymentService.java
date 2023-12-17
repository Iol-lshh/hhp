package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.service.component.PaymentComponent;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    ResultDto<PaymentDto> exchange(long userId, int toNeed) throws Exception;
    List<PaymentDto> findAll();
    Optional<PaymentDto> find(long id);
}
