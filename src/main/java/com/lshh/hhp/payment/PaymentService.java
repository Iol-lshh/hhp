package com.lshh.hhp.payment;

public interface PaymentService {
    PaymentDto exchange(long userId, int toNeed) throws Exception;
}
