package com.lshh.hhp.payment.service;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.payment.Payment;
import com.lshh.hhp.payment.dto.PaymentDto;
import com.lshh.hhp.payment.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Biz
public class PaymentBaseImpl implements PaymentBase {
    final PaymentRepository paymentRepository;


    @Override
    @Transactional
    public PaymentDto create(long userId, int toNeed) {
        Payment newOne = new Payment()
                .userId(userId)
                .into(toNeed);

        return PaymentBase.toDto(paymentRepository.save(newOne));
    }

}
