package com.lshh.hhp.point.service;

import com.lshh.hhp.common.Service;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.payment.Payment;
import com.lshh.hhp.point.Point;
import com.lshh.hhp.point.Point.PointType;
import com.lshh.hhp.point.VPoint;
import com.lshh.hhp.point.repository.PointRepository;
import com.lshh.hhp.point.repository.VPointRepository;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PointServiceImpl implements PointService {

    final PointRepository pointRepository;
    final VPointRepository vPointRepository;

    @Override
    @Transactional
    public Point add(Payment payment) {

        Point point = Point.createNewAddPoint(payment);
        return pointRepository.save(point);
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public List<Point> subtract(List<OrderItem> orderItems) throws Exception {
        List<Point> pointList = Point.createNewSubtractPoints(orderItems);
        
        // 잔액 확인
        if(!isSubtractable(orderItems.get(0).userId(), pointList.stream().mapToInt(Point::count).sum())){
            throw new Exception("포인트 부족");
        }

        return pointRepository.saveAll(pointList);
    }

    @Override
    @Transactional
    public boolean isSubtractable(long userId, int subtractNumber){
        return remain(userId) + subtractNumber >= 0;
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public List<Point> cancelSubtract(List<OrderItem> purchaseDtoList) throws Exception {
        List<Point> targetList = purchaseDtoList.stream()
                .map(purchase->pointRepository.findByFromTypeAndFromId(PointType.PURCHASE.ordinal(), purchase.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        Point.setDisable(targetList);

        return targetList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Point> findAllByUserId(long userId) {
        return pointRepository.findAllByUserId(userId);
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public List<Point> squash() {
        // 1. 각 user 마다, 이전 포인트들의 count를 0처리하고
        // 2. sum의 count인 포인트를 추가해줌
        // 의의: 0 처리 된 것을 백업하고 비울 수 있게된다. => payment와 purchase에 지불 정보는 남아있다.
        // # sum group by user_id
        List<Point> eachSums = vPointRepository
                .findAll()
                .stream()
                .map(Point::createNewSquashedPoint)
                .toList();

        // # update count = 0
        List<Point> all = pointRepository.findAll();
        all.forEach(Point::setDisable);
        pointRepository.saveAll(all);

        // # insert sum
        return pointRepository.saveAll(eachSums);
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public Point squash(long userId) throws Exception {
        Point sumed = vPointRepository
                .findByUserId(userId)
                .map(Point::createNewSquashedPoint)
                .orElseThrow(()->new Exception("압축할 포인트가 없습니다."));

        // # update count = 0
        List<Point> all = pointRepository.findAllByUserId(userId);
        all.forEach(Point::setDisable);
        pointRepository.saveAll(all);

        // # insert sum
        return pointRepository.save(sumed);
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
