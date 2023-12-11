package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.dto.PointDto;
import com.lshh.hhp.dto.PurchaseDto;

import java.util.List;
import java.util.Optional;

public interface PointService {

    enum PointType{
        PAYMENT(1),
        PURCHASE(2);

        PointType(int i) {}
    }
    ResultDto<PointDto> save(PointDto dto) throws Exception;

    List<PointDto> findAll();
    Optional<PointDto> find(long id);

    List<PointDto> findAllByUserId(long userId);

    default boolean isPayable(long userId, int toPay){
        return remain(userId) > toPay;
    }
    Integer remain(long userId);
    ResultDto<PointDto> payment(PaymentDto dto) throws Exception;
    ResultDto<PointDto> purchase(PurchaseDto dto) throws Exception;
}
