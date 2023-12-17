package com.lshh.hhp.service;

import com.lshh.hhp.service.component.PointComponent.PointType;
import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PaymentDto;
import com.lshh.hhp.dto.PointDto;
import com.lshh.hhp.dto.PurchaseDto;
import com.lshh.hhp.dto.RequestPurchaseDto;
import com.lshh.hhp.orm.entity.Point;
import com.lshh.hhp.orm.entity.VPoint;
import com.lshh.hhp.orm.repository.PointRepository;
import com.lshh.hhp.orm.repository.VPointRepository;
import com.lshh.hhp.service.component.PointComponent;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PointServiceDefault implements PointService {

    final PointRepository pointRepository;
    final VPointRepository vPointRepository;

    public static PointDto toDto(Point entity){
        return new PointDto()
                .id(entity.id())
                .userId(entity.userId())
                .fromType(entity.fromType())
                .fromId(entity.fromId())
                .count(entity.count());
    }
    public static Point toEntity(PointDto dto){
        return new Point()
                .id(dto.id())
                .userId(dto.userId())
                .fromType(dto.fromType())
                .fromId(dto.fromId())
                .count(dto.count());
    }
    @Override
    public ResultDto<PointDto> save(PointDto dto) throws Exception {
        dto = toDto(pointRepository.save(toEntity(dto)));
        return new ResultDto<>(Result.OK, dto);
    }

    @Override
    public List<PointDto> findAll() {
        return pointRepository
                .findAll()
                .stream()
                .map(PointServiceDefault::toDto)
                .toList();
    }

    @Override
    public Optional<PointDto> find(long id) {
        return pointRepository
                .findById(id)
                .map(PointServiceDefault::toDto);
    }

    @Override
    public List<PointDto> findAllByUserId(long userId) {
        return pointRepository
                .findAllByUserId(userId)
                .stream()
                .map(PointServiceDefault::toDto)
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
    public ResultDto<List<PointDto>> purchase(List<PurchaseDto> dtoList) throws Exception {

        List<Point> pointList = dtoList.stream()
                .map(dto->new Point()
                    .userId(dto.userId())
                    .count(dto.paid() * -1)
                    .fromId(dto.id())
                    .fromType(PointType.PURCHASE.ordinal()))
                .toList();

        List<PointDto> results = pointRepository
                .saveAllAndFlush(pointList)
                .stream()
                .map(PointServiceDefault::toDto)
                .toList();

        return new ResultDto<>(results);
    }

    @Override
    @Transactional
    public ResultDto<List<PointDto>> squash() {
        // 1. 각 user 마다, 이전 포인트들의 count를 0처리하고
        // 2. sum의 count인 포인트를 추가해줌
        // 의의: 0 처리 된 것을 백업하고 비울 수 있게된다. => payment와 purchase에 지불 정보는 남아있다.
        // # sum group by user_id
        List<Point> eachSums = vPointRepository
                .findAll()
                .stream()
                .map(vPoint -> new Point()
                        .userId(vPoint.userId())
                        .fromType(PointType.SUM.ordinal())
                        .count(vPoint.remain()))
                .toList();

        // # update count = 0
        List<Point> all = pointRepository
                .findAll()
                .stream()
                .map(el->el.count(0))
                .toList();
        pointRepository.saveAllAndFlush(all);

        // # insert sum
        eachSums = pointRepository.saveAllAndFlush(eachSums);

        return new ResultDto<>(eachSums.stream().map(PointServiceDefault::toDto).toList());
    }

    @Override
    public ResultDto<PointDto> squash(long userId) throws Exception {
        Point sum = vPointRepository
                .findByUserId(userId)
                .map(vPoint -> new Point()
                        .userId(vPoint.userId())
                        .fromType(PointType.SUM.ordinal())
                        .count(vPoint.remain()))
                .orElseThrow(Exception::new);

        // # update count = 0
        List<Point> all = pointRepository
                .findAllByUserId(userId)
                .stream()
                .map(el->el.count(0))
                .toList();
        pointRepository.saveAllAndFlush(all);

        // # insert sum
        sum = pointRepository.save(sum);

        return new ResultDto<>(toDto(sum));
    }

}
