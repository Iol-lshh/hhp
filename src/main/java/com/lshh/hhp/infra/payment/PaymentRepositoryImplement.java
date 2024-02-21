package com.lshh.hhp.infra.payment;

import com.lshh.hhp.domain.payment.PaymentRepository;
import com.lshh.hhp.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PaymentRepositoryImplement implements PaymentRepository {
    final PaymentJpaRepository paymentJpaRepository;

    @Override
    public List<Payment> findByUserId(long userId) {
        return paymentJpaRepository.findByUserId(userId);
    }

    @Override
    public Payment save(Payment newOne) {
        return paymentJpaRepository.save(newOne);
    }
}
