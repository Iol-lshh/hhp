package com.lshh.hhp.payment;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.point.PointBase;
import com.lshh.hhp.user.UserBase;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Biz(level = 1)
public class PaymentServiceImpl implements PaymentService {

    final UserBase userComponent;
    final PaymentBase paymentComponent;
    final PointBase pointComponent;

    @Override
    @Transactional
    public PaymentDto exchange(long userId, int toNeed) throws Exception {
        // 유저 확인
        userComponent.find(userId).orElseThrow(Exception::new);

        PaymentDto paymentDto = paymentComponent.create(userId, toNeed);

        pointComponent.pay(paymentDto);

        return paymentDto;
    }
}
