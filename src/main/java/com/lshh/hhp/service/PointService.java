package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.dto.PointDto;

import java.util.List;
import java.util.Optional;

public interface PointService {

    enum PointType{
        PAYMENT(1),
        PURCHASE(2);

        PointType(int i) {}
    }
    Result save(PointDto dto) throws Exception;

    List<PointDto> findAll();
    Optional<PointDto> find(long id);

    List<PointDto> findAllByUserId(long userId);

    Integer remain(long userId);
}
