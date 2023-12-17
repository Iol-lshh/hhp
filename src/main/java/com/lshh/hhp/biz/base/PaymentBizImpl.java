package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.origin.PaymentDto;
import com.lshh.hhp.orm.entity.Payment;
import com.lshh.hhp.orm.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PaymentBizImpl implements PaymentBiz {
    final PaymentRepository paymentRepository;


    @Override
    public PaymentDto create(long userId, int toNeed) {
        Payment newOne = new Payment()
                .userId(userId)
                .into(toNeed);

        return PaymentBiz.toDto(paymentRepository.save(newOne));
    }

}
