package com.lshh.hhp.biz.biz1;

import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.PaymentDto;
import com.lshh.hhp.dto.origin.PointDto;
import com.lshh.hhp.biz.base.PaymentBiz;
import com.lshh.hhp.biz.base.PointBiz;
import com.lshh.hhp.biz.base.UserBiz;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentBiz1Impl implements PaymentBiz1 {

    final UserBiz userComponent;
    final PaymentBiz paymentComponent;
    final PointBiz pointComponent;

    @Override
    @Transactional
    public ResultDto<PaymentDto> exchange(long userId, int toNeed) throws Exception {
        // 유저 확인
        userComponent.find(userId).orElseThrow(Exception::new);

        PaymentDto paymentDto = paymentComponent.create(userId, toNeed);

        PointDto pointDto = pointComponent.pay(paymentDto);

        return new ResultDto<>(paymentDto);
    }
}
