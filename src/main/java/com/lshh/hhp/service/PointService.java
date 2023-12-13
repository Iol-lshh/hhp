package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface PointService {

    enum PointType{
        SUM(0),
        PAYMENT(1),
        PURCHASE(2);

        PointType(int code) {}
        public static PointType of(final int code){
            return PointType.values()[code];
        }
    }

    ResultDto<PointDto> save(PointDto dto) throws Exception;

    List<PointDto> findAll();
    Optional<PointDto> find(long id);

    List<PointDto> findAllByUserId(long userId);

    Integer remain(long userId);
    ResultDto<PointDto> payment(PaymentDto dto) throws Exception;
    ResultDto<List<PointDto>> purchase(List<PurchaseDto> dtoList) throws Exception;
    ResultDto<List<PointDto>> squash();
    ResultDto<PointDto> squash(long userId) throws Exception;
}
