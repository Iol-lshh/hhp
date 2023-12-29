package com.lshh.hhp.biz.base;

import com.lshh.hhp.biz.Biz;
import com.lshh.hhp.dto.origin.PaymentDto;
import com.lshh.hhp.orm.entity.Payment;
import com.lshh.hhp.orm.repository.PaymentRepository;
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
