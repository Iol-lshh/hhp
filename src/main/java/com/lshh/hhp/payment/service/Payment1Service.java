package com.lshh.hhp.payment.service;

import com.lshh.hhp.payment.dto.PaymentDto;

/**
 * level 0
 * Represents a service for processing payments.
 */
public interface Payment1Service {
    PaymentDto exchange(long userId, int toNeed) throws Exception;
}
