package com.lshh.hhp.point.service;

import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.payment.Payment;
import com.lshh.hhp.point.Point;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * level 0
 * PointService interface provides methods for managing user points.
 */
public interface PointService {

    Point add(Payment payment);

    List<Point> subtract(List<OrderItem> orderItems) throws Exception;

    @Transactional
    boolean isSubtractable(long userId, int subtractNumber);

    List<Point> cancelSubtract(List<OrderItem> purchaseDtoList) throws Exception;

    List<Point> findAllByUserId(long userId);

    List<Point> squash();
    Point squash(long userId) throws Exception;

    int remain(long userId);
}
