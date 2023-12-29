package com.lshh.hhp.payment;

import com.lshh.hhp.common.Biz;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Biz
public class PaymentBizImpl implements PaymentBiz {
    final PaymentRepository paymentRepository;


    @Override
    @Transactional
    public PaymentDto create(long userId, int toNeed) {
        Payment newOne = new Payment()
                .userId(userId)
                .into(toNeed);

        return PaymentBiz.toDto(paymentRepository.save(newOne));
    }

}
