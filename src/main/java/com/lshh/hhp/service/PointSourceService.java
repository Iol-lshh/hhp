package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.PointSourceDto;

import java.util.List;
import java.util.Optional;

public interface PointSourceService {
    Response.Result save(PointSourceDto dto);

    Optional<PointSourceDto> find();
    List<PointSourceDto> findAll();
}
