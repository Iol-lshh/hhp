package com.lshh.hhp.service.component;

import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.orm.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentComponent {

    static PaymentDto toDto(Payment entity){
        return new PaymentDto()
                .id(entity.id())
                .into(entity.into())
                .userId(entity.userId());
    }
    static Payment toEntity(PaymentDto dto){
        return new Payment()
                .id(dto.id())
                .into(dto.into())
                .userId(dto.userId());
    }

    List<PaymentDto> findAll();
    Optional<PaymentDto> find(long id);
    PaymentDto save(PaymentDto dto);
}
