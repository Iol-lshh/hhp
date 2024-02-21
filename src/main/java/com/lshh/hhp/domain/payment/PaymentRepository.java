package com.lshh.hhp.domain.payment;

import java.util.List;

public interface PaymentRepository {

    List<Payment> findByUserId(long userId);

    Payment save(Payment newOne);
}
