package com.lshh.hhp.payment.service;

import com.lshh.hhp.payment.dto.PaymentDto;

public interface PaymentService {
    PaymentDto exchange(long userId, int toNeed) throws Exception;
}
