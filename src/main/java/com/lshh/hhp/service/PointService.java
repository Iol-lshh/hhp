package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.*;
import com.lshh.hhp.service.component.PointComponent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface PointService {
    Integer remain(long userId);
    ResultDto<List<PointDto>> purchase(List<PurchaseDto> dtoList) throws Exception;
    ResultDto<List<PointDto>> squash();
    ResultDto<PointDto> squash(long userId) throws Exception;
    List<PointDto> findAll();
}
