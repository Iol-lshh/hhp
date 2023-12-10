package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.PaymentDto;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Response.Result save(PaymentDto dto);

    List<PaymentDto> findAll();
    Optional<PaymentDto> find(long userId);
}
