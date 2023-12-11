package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.orm.entity.Payment;
import com.lshh.hhp.orm.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PaymentServiceDefault implements PaymentService{

    final UserService userService;
    final PointService pointService;
    final PaymentRepository paymentRepository;

    public PaymentDto toDto(Payment entity){
        return new PaymentDto()
            .id(entity.id())
            .into(entity.into())
            .userId(entity.userId());
    }
    public Payment toEntity(PaymentDto dto){
        return new Payment()
            .id(dto.id())
            .into(dto.into())
            .userId(dto.userId());
    }

    @Override
    @Transactional
    public ResultDto<PaymentDto> exchange(long userId, int toNeed) throws Exception {
        userService.find(userId).orElseThrow(Exception::new);

        Payment payment = new Payment()
            .userId(userId)
            .into(toNeed);
        payment = paymentRepository.save(payment);
        PaymentDto paymentDto = toDto(payment);
        pointService.payment(paymentDto);

        return new ResultDto<>(Result.OK, paymentDto);
    }

    @Override
    public List<PaymentDto> findAll() {
        return paymentRepository
            .findAll()
            .stream()
            .map(this::toDto)
            .toList();
    }

    @Override
    public Optional<PaymentDto> find(long id) {
        return paymentRepository.findById(id)
            .map(this::toDto);
    }
}
