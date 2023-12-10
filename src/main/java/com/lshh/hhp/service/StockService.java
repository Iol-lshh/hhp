package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface StockService {
    Response.Result save(UserDto dto);

    List<UserDto> findAll();
    Optional<UserDto> find(long userId);
}
