package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.PurchaseDto;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {
    Response.Result save(PurchaseDto dto);

    Optional<PurchaseDto> find();
    List<PurchaseDto> findAll();
}
