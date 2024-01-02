package com.lshh.hhp.point;

import com.lshh.hhp.payment.PaymentDto;
import com.lshh.hhp.purchase.PurchaseDto;

import java.util.List;

public interface PointBase {

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

    List<PointDto> purchase(List<PurchaseDto> purchaseDtos) throws Exception;
    List<PointDto> cancel(List<PurchaseDto> purchaseDtoList) throws Exception;

    PointDto save(PointDto dto);

    List<PointDto> findAllByUserId(long userId);

    List<PointDto> squash();
    PointDto squash(long userId) throws Exception;

    int remain(long userId);
}
