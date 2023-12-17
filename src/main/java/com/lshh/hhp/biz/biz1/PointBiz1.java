package com.lshh.hhp.biz.biz1;

import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.PointDto;

import java.util.List;

public interface PointBiz1 {
    List<PointDto> findAllByUserId(long userId);

    Integer remain(long userId);

    ResultDto<List<PointDto>> squash();
    ResultDto<PointDto> squash(long userId) throws Exception;
}
