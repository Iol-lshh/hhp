package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PurchaseDto;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {
    ResultDto<PurchaseDto> purchase(long userId, long productId) throws Exception;

    Optional<PurchaseDto> find(long id);
    List<PurchaseDto> findAll();

}
