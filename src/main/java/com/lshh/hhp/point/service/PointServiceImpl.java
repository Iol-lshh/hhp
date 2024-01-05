package com.lshh.hhp.point.service;

import com.lshh.hhp.common.Service;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.payment.Payment;
import com.lshh.hhp.point.Point;
import com.lshh.hhp.point.Point.PointType;
import com.lshh.hhp.point.repository.PointRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PointServiceImpl implements PointService {

    final PointRepository pointRepository;

    @Override
    @Transactional
    public Point add(Payment payment) {

        Point point = Point.createNewAddPoint(payment);
        return pointRepository.save(point);
    }

    @Override
    @Transactional
    public List<Point> subtractByOrderItems(List<OrderItem> orderItems) throws Exception {
        long userId = orderItems.get(0).userId();
        // 잔액 확인
        int sumToPay = orderItems.stream().mapToInt(OrderItem::toPay).sum();
        List<Point> targetList = pointRepository.findAllByUserIdWithLock(userId);
        int remain = targetList.stream().mapToInt(Point::count).sum();
        if(remain - sumToPay < 0){
            throw new Exception("포인트 부족 " + remain +", "+ sumToPay);
        }

        // 차감 포인트 저장
        List<Point> pointList = Point.createNewSubtractPoints(orderItems);
        return pointRepository.saveAll(pointList);
    }

    @Override
    @Transactional
    public List<Point> cancelSubtract(List<OrderItem> orderItems) throws Exception {
        // orderItem들에 대한 포인트들을 전부 찾기
        List<Point> targetList = findAllByOrderItems(orderItems);
        Point.setDisable(targetList);

        return targetList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Point> findAllByOrderItems(List<OrderItem> orderItems){
        return orderItems.stream()
                .map(orderItem -> pointRepository.findByFromTypeAndFromIdWithLock(PointType.PURCHASE.ordinal(), orderItem.id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Point> findAllByUserId(long userId) {
        return pointRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Point squash(long userId) throws Exception {
        // # 잔액 확인
        int remain = pointRepository.findAllByUserIdWithLock(userId).stream().mapToInt(Point::count).sum();
        if(remain == 0){
            throw new Exception("압축할 포인트가 없습니다.");
        }

        // # 압축
        Point sumed = Point.createNewSquashedPoint(userId, remain);

        // # 이전 포인트 비활성화
        List<Point> userPointList = pointRepository.findAllByUserIdWithLock(userId);
        userPointList.forEach(Point::setDisable);
        pointRepository.saveAll(userPointList);

        // # 압축본 저장
        return pointRepository.save(sumed);
    }

    @Override
    @Transactional(readOnly = true)
    public int countRemain(long userId) {
        List<Point> userPointList = pointRepository.findAllByUserIdWithLock(userId);
        return userPointList.stream().mapToInt(Point::count).sum();
    }
}
