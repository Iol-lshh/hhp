package com.lshh.hhp.payment.service;

import com.lshh.hhp.common.annotation.Service;
import com.lshh.hhp.payment.Payment;
import com.lshh.hhp.payment.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment create(long userId, int toNeed) {
        Payment newOne = Payment.createNewPayment(userId, toNeed);

        return paymentRepository.save(newOne);
    }
}
