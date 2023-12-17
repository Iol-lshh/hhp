package com.lshh.hhp.service.component;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.orm.entity.Payment;
import com.lshh.hhp.orm.repository.PaymentRepository;
import com.lshh.hhp.service.PaymentServiceDefault;
import com.lshh.hhp.service.PointService;
import com.lshh.hhp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentComponentDefault implements PaymentComponent{

    final PaymentRepository paymentRepository;

    @Override
    public List<PaymentDto> findAll() {
        return paymentRepository
                .findAll()
                .stream()
                .map(PaymentComponent::toDto)
                .toList();
    }

    @Override
    public Optional<PaymentDto> find(long id) {
        return paymentRepository.findById(id)
                .map(PaymentComponent::toDto);
    }

    @Override
    public PaymentDto save(PaymentDto dto) {
        return PaymentComponent.toDto(paymentRepository.save(PaymentComponent.toEntity(dto)));
    }
}
