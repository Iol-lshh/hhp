package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.StockDto;
import com.lshh.hhp.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface StockService {
    Response.Result save(StockDto dto);

    List<StockDto> findAll();
    Optional<StockDto> find(long id);

    List<StockDto> findAllByProductId(long productId);
}
