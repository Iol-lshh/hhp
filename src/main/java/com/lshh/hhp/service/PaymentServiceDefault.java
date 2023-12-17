package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.orm.entity.Payment;
import com.lshh.hhp.orm.repository.PaymentRepository;
import com.lshh.hhp.service.component.PaymentComponent;
import com.lshh.hhp.service.component.UserComponent;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PaymentServiceDefault implements PaymentService{

    final UserComponent userComponent;
    final PaymentComponent pointComponent;
    final PaymentComponent paymentComponent;

    @Override
    @Transactional
    public ResultDto<PaymentDto> exchange(long userId, int toNeed) throws Exception {
        userComponent.find(userId).orElseThrow(Exception::new);

        PaymentDto payment = new PaymentDto()
            .userId(userId)
            .into(toNeed);
        payment = paymentComponent.save(payment);

        pointComponent.payment(payment);

        return new ResultDto<>(payment);
    }

    @Override
    public List<PaymentDto> findAll() {
        return paymentComponent
            .findAll();
    }

    @Override
    public Optional<PaymentDto> find(long id) {
        return paymentComponent.find(id);
    }
}
