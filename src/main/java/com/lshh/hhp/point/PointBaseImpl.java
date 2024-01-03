package com.lshh.hhp.point;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.payment.PaymentDto;
import com.lshh.hhp.purchase.PurchaseDto;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Biz
public class PointBaseImpl implements PointBase {

    final PointRepository pointRepository;
    final VPointRepository vPointRepository;

    @Override
    @Transactional
    public PointDto pay(PaymentDto paymentDto) {
        PointDto pointDto = new PointDto()
                .userId(paymentDto.userId())
                .count(paymentDto.into())
                .fromId(paymentDto.id())
                .fromType(PointType.PAYMENT.ordinal());

        return this.save(pointDto);
    }

    @Override
    @Transactional
    public PointDto save(PointDto dto) {
        return PointBase.toDto(pointRepository.save(PointBase.toEntity(dto)));
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Transactional
    public List<PointDto> purchase(List<PurchaseDto> purchaseDtos) throws Exception {
        List<Point> pointList = purchaseDtos.stream()
                .map(dto->new Point()
                        .userId(dto.userId())
                        .count(dto.paid() * -1)
                        .fromId(dto.id())
                        .fromType(PointType.PURCHASE.ordinal()))
                .toList();
        if(remain(purchaseDtos.get(0).userId()) + pointList.stream().mapToInt(Point::count).sum() < 0){
            throw new Exception("포인트 부족");
        }

        return pointRepository
                .saveAll(pointList)
                .stream()
                .map(PointBase::toDto)
                .toList();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Transactional
    public List<PointDto> cancelPurchase(List<PurchaseDto> purchaseDtoList) throws Exception {
        List<Point> pointList = purchaseDtoList.stream()
                .map(purchase->pointRepository.findByFromTypeAndFromId(PointType.PURCHASE.ordinal(), purchase.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(point -> point.count(0))
                .toList();
        return pointRepository.saveAll(pointList)
                .stream()
                .map(PointBase::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PointDto> findAllByUserId(long userId) {
        return pointRepository
                .findAllByUserId(userId)
                .stream()
                .map(PointBase::toDto)
                .toList();
    }

    @Override
    @Transactional
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
                        .fromType(PointBase.PointType.SUM.ordinal())
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

        return eachSums.stream().map(PointBase::toDto).toList();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Transactional
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

        return PointBase.toDto(sum);
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Transactional(readOnly = true)
    public int remain(long userId) {
        return vPointRepository
                .findById(userId)
                .map(VPoint::remain)
                .orElse(0);
    }

}
