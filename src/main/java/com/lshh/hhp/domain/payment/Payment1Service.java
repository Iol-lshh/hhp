package com.lshh.hhp.domain.payment;

import com.lshh.hhp.common.annotation.Service;
import com.lshh.hhp.common.exception.BusinessException;
import com.lshh.hhp.domain.user.UserService;
import com.lshh.hhp.domain.payment.dto.PaymentDto;
import com.lshh.hhp.domain.point.PointService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Service(level = 1)
public class Payment1Service {

    final UserService userComponent;
    final PaymentService paymentComponent;
    final PointService pointComponent;
    final PaymentRepository paymentRepository;

    @Transactional
    public PaymentDto exchange(long userId, int toNeed) throws Exception {
        // 유저 확인
        userComponent.find(userId).orElseThrow(()->new BusinessException(String.format("""
        { PaymentDto::exchange - 유저 확인 불가 {"userId": "%s", "toNeed": "%d"} }""", userId, toNeed)));

        Payment payment = paymentComponent.create(userId, toNeed);

        pointComponent.add(payment);

        return payment.toDto();
    }

    public List<Payment> findByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
}