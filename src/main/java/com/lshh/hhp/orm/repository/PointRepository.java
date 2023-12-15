package com.lshh.hhp.orm.repository;

import com.lshh.hhp.orm.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository  extends JpaRepository<Point, Long> {
    List<Point> findAllByUserId(Long userId);
}
