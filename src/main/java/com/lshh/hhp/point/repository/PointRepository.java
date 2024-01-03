package com.lshh.hhp.point.repository;

import com.lshh.hhp.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointRepository  extends JpaRepository<Point, Long> {
    List<Point> findAllByUserId(Long userId);
    Optional<Point> findByFromTypeAndFromId(Integer fromType, Long fromId);
}
