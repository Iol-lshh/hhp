package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.origin.PaymentDto;
import com.lshh.hhp.orm.entity.Payment;

public interface PaymentBiz {

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

    PaymentDto create(long userId, int toNeed);
}
