package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.PaymentDto;
import com.lshh.hhp.dto.origin.PointDto;
import com.lshh.hhp.dto.origin.PurchaseDto;
import com.lshh.hhp.orm.entity.Point;

import java.util.List;

public interface PointBiz {

    enum PointType{
        SUM(0),
        PAYMENT(1),
        PURCHASE(2);

        PointType(int code) {}
        public static PointType of(final int code){
            return PointType.values()[code];
        }
    }

    static PointDto toDto(Point entity){
        return new PointDto()
                .id(entity.id())
                .userId(entity.userId())
                .fromType(entity.fromType())
                .fromId(entity.fromId())
                .count(entity.count());
    }
    static Point toEntity(PointDto dto){
        return new Point()
                .id(dto.id())
                .userId(dto.userId())
                .fromType(dto.fromType())
                .fromId(dto.fromId())
                .count(dto.count());
    }
    PointDto pay(PaymentDto paymentDto);

    List<PointDto> purchase(List<PurchaseDto> purchaseDtos);

    PointDto save(PointDto dto);

    List<PointDto> findAllByUserId(long userId);

    ResultDto<List<PointDto>> squash();
    ResultDto<PointDto> squash(long userId) throws Exception;

    int remain(long userId);
}
