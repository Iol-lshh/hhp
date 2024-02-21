package com.lshh.hhp.domain.payment;

import com.lshh.hhp.common.annotation.Service;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PaymentService {

    final PaymentRepository paymentRepository;

    @Transactional
    public Payment create(long userId, int toNeed) {
        Payment newOne = Payment.createNewPayment(userId, toNeed);

        return paymentRepository.save(newOne);
    }
}
