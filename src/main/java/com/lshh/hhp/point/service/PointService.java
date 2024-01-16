package com.lshh.hhp.point.service;

import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.payment.Payment;
import com.lshh.hhp.point.Point;
import com.lshh.hhp.product.dto.EventSquashPointDto;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * level 0
 * PointService interface provides methods for managing user points.
 */
public interface PointService {

    Point add(Payment payment);

    List<Point> subtractByOrderItems(long userId, List<OrderItem> orderItems);

    List<Point> save(long userId, List<Point> pointList);

    @Transactional
    Point save(Point point);

    List<Point> findAllByUserId(long userId);

    @EventListener
    void onSquashPointEvent(EventSquashPointDto event);

    Point squash(long userId);

    int countRemain(long userId);

    int lockUserPoint(long userId);

    int clear(long userId);
}
