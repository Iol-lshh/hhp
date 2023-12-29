package com.lshh.hhp.payment;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.point.PointBiz;
import com.lshh.hhp.user.UserBiz;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Biz(level = 1)
public class PaymentBiz1Impl implements PaymentBiz1 {

    final UserBiz userComponent;
    final PaymentBiz paymentComponent;
    final PointBiz pointComponent;

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
