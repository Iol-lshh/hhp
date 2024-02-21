package com.lshh.hhp.domain.point;

import java.util.List;
import java.util.Optional;

public interface PointRepository {

    List<Point> findAllByUserId(Long userId);

    List<Point> findAllByUserIdWithLock(Long userId);

    Optional<Point> findByFromTypeAndFromIdWithLock(Integer fromType, Long fromId);

    void deleteByUserId(Long userId);

    List<Point> saveAllAndFlush(List<Point> pointList);

    Point save(Point point);
}
