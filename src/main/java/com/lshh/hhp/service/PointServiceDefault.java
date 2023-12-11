package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.dto.PointDto;
import com.lshh.hhp.dto.PurchaseDto;
import com.lshh.hhp.orm.entity.Point;
import com.lshh.hhp.orm.entity.VPoint;
import com.lshh.hhp.orm.repository.PointRepository;
import com.lshh.hhp.orm.repository.VPointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PointServiceDefault implements PointService {

    final PointRepository pointRepository;
    final VPointRepository vPointRepository;

    public PointDto toDto(Point entity){
        return new PointDto()
                .id(entity.id())
                .userId(entity.userId())
                .fromType(entity.fromType())
                .fromId(entity.fromId())
                .count(entity.count());
    }
    public Point toEntity(PointDto dto){
        return new Point()
                .id(dto.id())
                .userId(dto.userId())
                .fromType(dto.fromType())
                .fromId(dto.fromId())
                .count(dto.count());
    }
    @Override
    public ResultDto<PointDto> save(PointDto dto) throws Exception {
        dto = this.toDto(pointRepository.save(toEntity(dto)));
        return new ResultDto<>(Result.OK, dto);
    }

    @Override
    public List<PointDto> findAll() {
        return pointRepository
                .findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Optional<PointDto> find(long id) {
        return pointRepository
                .findById(id)
                .map(this::toDto);
    }

    @Override
    public List<PointDto> findAllByUserId(long userId) {
        return pointRepository
                .findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Integer remain(long userId) {
        return vPointRepository
                .findById(userId)
                .map(VPoint::remain)
                .orElse(0);
    }

    @Override
    public ResultDto<PointDto> payment(PaymentDto dto) throws Exception {
        PointDto pointDto = new PointDto()
                .userId(dto.userId())
                .count(dto.into())
                .fromId(dto.id())
                .fromType(PointType.PAYMENT.ordinal());
        return this.save(pointDto);
    }

    @Override
    public ResultDto<PointDto> purchase(PurchaseDto dto) throws Exception {
        PointDto pointDto = new PointDto()
                .userId(dto.userId())
                .count(dto.paid() * -1)
                .fromId(dto.id())
                .fromType(PointService.PointType.PURCHASE.ordinal());
        return this.save(pointDto);
    }

}
