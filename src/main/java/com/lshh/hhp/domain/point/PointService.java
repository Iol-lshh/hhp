package com.lshh.hhp.domain.point;

import com.lshh.hhp.common.annotation.DistributedLock;
import com.lshh.hhp.common.annotation.Service;
import com.lshh.hhp.common.exception.BusinessException;
import com.lshh.hhp.domain.order.item.OrderItem;
import com.lshh.hhp.domain.payment.Payment;
import com.lshh.hhp.domain.product.dto.EventSquashPointDto;
import lombok.AllArgsConstructor;
//import org.springframework.cache.annotation.CachePut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class PointService {

    final PointRepository pointRepository;

    final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Point add(Payment payment) {
//        pointRepository.findAllByUserIdWithLock(payment.userId());
        Point point = Point.createNewAddPoint(payment);

        return save(point);
    }



    @Transactional
    public List<Point> subtractByOrderItems(long userId, List<OrderItem> orderItems) {
        // # 총 차감액
        int sumToSubtract = orderItems.stream().mapToInt(OrderItem::toPay).sum();
        // # 락 획득
        lockUserPoint(userId);
        // # 잔고 확인
        int remain = countRemain(userId);
        // todo - 정상 락의 차감 확인

        // # 차감 가능 여부 확인
        if(remain - sumToSubtract < 0){
            throw new BusinessException(String.format("""
                    PointServiceImpl::subtractByOrderItems  - 차감 불가 remain: %s, sumToPay: %s, {%s}"""
                    , remain, sumToSubtract, String.join(",", orderItems.stream().map(e->e.id().toString()).toList())));
        }

        // # 차감 포인트 저장
        List<Point> pointList = Point.createNewSubtractPoints(orderItems);
        return save(userId, pointList);
    }

    @Transactional
    public List<Point> save(long userId, List<Point> pointList){
        try{
            return pointRepository.saveAllAndFlush(pointList);
        }finally {
            // 이전 포인트 내역 정리 이벤트 발행
            eventPublisher.publishEvent(EventSquashPointDto.of(userId));
        }
    }

    @Transactional
    public Point save(Point point){
        try{
            return pointRepository.save(point);
        }finally {
            // 이전 포인트 내역 정리 이벤트 발행
            eventPublisher.publishEvent(EventSquashPointDto.of(point.userId()));
        }
    }

    @Transactional(readOnly = true)
    public List<Point> findAllByUserId(long userId) {
        return pointRepository.findAllByUserId(userId);
    }


    @Async
    @EventListener
    public void onSquashPointEvent(EventSquashPointDto event) {
        squash(event.userId());
    }

    @Transactional
    public Point squash(long userId) {
        // # 잔액 확인
        lockUserPoint(userId);
        int remain = countRemain(userId);
        // # 압축
        Point sumed = Point.createNewSquashedPoint(userId, remain);

        // # 이전 포인트 비활성화
        clear(userId);

        // # 압축본 저장
        return pointRepository.save(sumed);
    }

    @Transactional(readOnly = true)
    public int countRemain(long userId) {
        List<Point> userPointList = pointRepository.findAllByUserId(userId);
        return userPointList.stream().mapToInt(Point::count).sum();
    }


    @Transactional
    @DistributedLock(key="point_"+"#userId")
//    @CachePut(cacheNames="user_point", key="#userId")
    public int lockUserPoint(long userId) {
        return 1;
    }

    @Transactional
    public int clear(long userId) {
        pointRepository.deleteByUserId(userId);
        return 1;
    }
}