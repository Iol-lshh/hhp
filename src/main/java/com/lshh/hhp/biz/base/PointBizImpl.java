package com.lshh.hhp.biz.base;

import com.lshh.hhp.biz.Biz;
import com.lshh.hhp.dto.origin.PaymentDto;
import com.lshh.hhp.dto.origin.PointDto;
import com.lshh.hhp.dto.origin.PurchaseDto;
import com.lshh.hhp.orm.entity.Point;
import com.lshh.hhp.orm.entity.VPoint;
import com.lshh.hhp.orm.repository.PointRepository;
import com.lshh.hhp.orm.repository.VPointRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Biz
public class PointBizImpl implements PointBiz {

    final PointRepository pointRepository;
    final VPointRepository vPointRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PointDto pay(PaymentDto paymentDto) {
        PointDto pointDto = new PointDto()
                .userId(paymentDto.userId())
                .count(paymentDto.into())
                .fromId(paymentDto.id())
                .fromType(PointType.PAYMENT.ordinal());

        return this.save(pointDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PointDto save(PointDto dto) {
        return PointBiz.toDto(pointRepository.save(PointBiz.toEntity(dto)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public List<PointDto> purchase(List<PurchaseDto> purchaseDtos) throws Exception {
        List<Point> pointList = purchaseDtos.stream()
                .map(dto->new Point()
                        .userId(dto.userId())
                        .count(dto.paid() * -1)
                        .fromId(dto.id())
                        .fromType(PointType.PURCHASE.ordinal()))
                .toList();
        System.out.println(remain(purchaseDtos.get(0).userId()));
        System.out.println(pointList.stream().mapToInt(Point::count).sum());
        if(remain(purchaseDtos.get(0).userId()) + pointList.stream().mapToInt(Point::count).sum() < 0){
            throw new Exception("포인트 부족");
        }

        return pointRepository
                .saveAll(pointList)
                .stream()
                .map(PointBiz::toDto)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public List<PointDto> cancel(List<PurchaseDto> purchaseDtoList) throws Exception {
        List<Point> pointList = purchaseDtoList.stream()
                .map(purchase->pointRepository.findByFromTypeAndFromId(PointType.PURCHASE.ordinal(), purchase.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(point -> point.count(0))
                .toList();
        return pointRepository.saveAll(pointList)
                .stream()
                .map(PointBiz::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointDto> findAllByUserId(long userId) {
        return pointRepository
                .findAllByUserId(userId)
                .stream()
                .map(PointBiz::toDto)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PointDto> squash() {
        // 1. 각 user 마다, 이전 포인트들의 count를 0처리하고
        // 2. sum의 count인 포인트를 추가해줌
        // 의의: 0 처리 된 것을 백업하고 비울 수 있게된다. => payment와 purchase에 지불 정보는 남아있다.
        // # sum group by user_id
        List<Point> eachSums = vPointRepository
                .findAll()
                .stream()
                .map(vPoint -> new Point()
                        .userId(vPoint.userId())
                        .fromType(PointBiz.PointType.SUM.ordinal())
                        .count(vPoint.remain()))
                .toList();

        // # update count = 0
        List<Point> all = pointRepository
                .findAll()
                .stream()
                .map(el->el.count(0))
                .toList();
        pointRepository.saveAll(all);

        // # insert sum
        eachSums = pointRepository.saveAll(eachSums);

        return eachSums.stream().map(PointBiz::toDto).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PointDto squash(long userId) throws Exception {
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
        pointRepository.saveAll(all);

        // # insert sum
        sum = pointRepository.save(sum);

        return PointBiz.toDto(sum);
    }

    @Override
    @Transactional(readOnly = true)
    public int remain(long userId) {
        return vPointRepository
                .findById(userId)
                .map(VPoint::remain)
                .orElse(0);
    }

}
