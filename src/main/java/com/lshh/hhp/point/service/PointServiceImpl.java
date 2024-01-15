package com.lshh.hhp.point.service;

import com.lshh.hhp.common.Service;
import com.lshh.hhp.common.exception.BusinessException;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.payment.Payment;
import com.lshh.hhp.point.Point;
import com.lshh.hhp.point.Point.PointType;
import com.lshh.hhp.point.repository.PointRepository;
import com.lshh.hhp.product.dto.EventSquashPointDto;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PointServiceImpl implements PointService {

    final PointRepository pointRepository;

    final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Point add(Payment payment) {
//        pointRepository.findAllByUserIdWithLock(payment.userId());
        Point point = Point.createNewAddPoint(payment);
        return pointRepository.save(point);
    }

    @Override
    @Transactional
    public List<Point> subtractByOrderItems(List<OrderItem> orderItems) throws Exception {
        long userId = orderItems.get(0).userId();
        // # 총 차감액
        int sumToPay = orderItems.stream().mapToInt(OrderItem::toPay).sum();
        // # 락 획득
        List<Point> pointsForLock = pointRepository.findAllByUserIdWithLock(userId);

        // # 잔고 확인
        int remain = countRemain(userId);
        // # 차감 가능 여부 확인
        if(remain - sumToPay < 0){
            throw new BusinessException(String.format("""
                    PointServiceImpl::subtractByOrderItems  - 차감 불가 remain: %s, sumToPay: %s, {%s}"""
                    , remain, sumToPay, String.join(",", orderItems.stream().map(e->e.id().toString()).toList())));
        }
        // # 차감 포인트 저장
        List<Point> pointList = Point.createNewSubtractPoints(orderItems);

        // 새로운 주문을 먼저 받을 수 있도록, 락 범위 너머의 리스트로 새로 제공
        if(pointsForLock.size() > 10){
            eventPublisher.publishEvent(EventSquashPointDto.of(userId));

        }
        
        return pointRepository.saveAll(pointList);
    }

    @Override
    @Transactional
    public List<Point> cancelSubtract(List<OrderItem> orderItems) {
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


    @EventListener
    public void onSquashPointEvent(EventSquashPointDto event) throws Exception {
        squash(event.userId());
    }

    @Override
    @Transactional
    public Point squash(long userId) throws Exception {
        // # 잔액 확인
        int remain = pointRepository.findAllByUserIdWithLock(userId).stream().mapToInt(Point::count).sum();
        if(remain == 0){
            throw new BusinessException(String.format("""
                    PointServiceImpl::squash  - 잔액 0원 remain: %s, {%s}""", remain, userId));
        }

        // # 압축
        Point sumed = Point.createNewSquashedPoint(userId, remain);

        // # 이전 포인트 비활성화
        List<Point> userPointList = pointRepository.findAllByUserIdWithLock(userId);
        userPointList.forEach(Point::setDisable);
        pointRepository.saveAll(userPointList);
        if(userPointList.size()>20){
            clearZeroPoints(userId);
        }

        // # 압축본 저장
        return pointRepository.save(sumed);
    }

    @Override
//    @Transactional(readOnly = true)
    @Transactional
    public int countRemain(long userId) {
        List<Point> userPointList = pointRepository.findAllByUserId(userId);
        return userPointList.stream().mapToInt(Point::count).sum();
    }

    @Override
    @Transactional
    public int clearZeroPoints(long userId) {
        pointRepository.deleteByUserId(userId);
        return 1;
    }
}
