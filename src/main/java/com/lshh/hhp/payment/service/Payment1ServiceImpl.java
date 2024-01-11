package com.lshh.hhp.payment.service;

import com.lshh.hhp.common.Service;
import com.lshh.hhp.common.exception.BusinessException;
import com.lshh.hhp.payment.Payment;
import com.lshh.hhp.payment.dto.PaymentDto;
import com.lshh.hhp.point.service.PointService;
import com.lshh.hhp.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service(level = 1)
public class Payment1ServiceImpl implements Payment1Service {

    final UserService userComponent;
    final PaymentService paymentComponent;
    final PointService pointComponent;

    @Override
    @Transactional
    public PaymentDto exchange(long userId, int toNeed) throws Exception {
        // 유저 확인
        userComponent.find(userId).orElseThrow(()->new BusinessException(String.format("""
        { PaymentDto::exchange - 유저 확인 불가 {"userId": "%s", "toNeed": "%d"} }""", userId, toNeed)));

        Payment payment = paymentComponent.create(userId, toNeed);

        pointComponent.add(payment);

        return payment.toDto();
    }
}
