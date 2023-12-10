package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.PointDto;

import java.util.List;
import java.util.Optional;

public interface PointService {
    enum PointType{
        PURCHASE,
        PAYMENT
    }
    Response.Result save(PointDto dto);

    List<PointDto> findAll();
    Optional<PointDto> find(long userId);
}
