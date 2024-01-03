package com.lshh.hhp.point.service;

import com.lshh.hhp.payment.dto.PaymentDto;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.point.Point;
import com.lshh.hhp.point.dto.PointDto;

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
    PointDto add(PaymentDto paymentDto);

    List<PointDto> subtract(List<OrderItemDto> purchaseDtos) throws Exception;
    List<PointDto> cancelSubtract(List<OrderItemDto> purchaseDtoList) throws Exception;

    PointDto save(PointDto dto);

    List<PointDto> findAllByUserId(long userId);

    List<PointDto> squash();
    PointDto squash(long userId) throws Exception;

    int remain(long userId);
}
