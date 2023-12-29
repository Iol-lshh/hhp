package com.lshh.hhp.payment;

public interface PaymentBiz1 {
    PaymentDto exchange(long userId, int toNeed) throws Exception;
}
