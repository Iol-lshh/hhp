package com.lshh.hhp.service.component;

import com.lshh.hhp.dto.PaymentDto;

import java.util.List;
import java.util.Optional;

public interface PaymentComponent {
    List<PaymentDto> findAll();
    Optional<PaymentDto> find(long id);
}
