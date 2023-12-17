package com.lshh.hhp.service.component;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PointDto;

import java.util.List;
import java.util.Optional;

public interface PointComponent {
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
}
