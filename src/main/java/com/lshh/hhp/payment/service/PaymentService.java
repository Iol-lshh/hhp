package com.lshh.hhp.payment.service;

import com.lshh.hhp.payment.Payment;

/**
 * level 0
 * Represents a service for creating a payment.
 */
public interface PaymentService {

    Payment create(long userId, int toNeed);
}
