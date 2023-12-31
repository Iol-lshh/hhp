package com.lshh.hhp.point.repository;

import com.lshh.hhp.point.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointRepository  extends JpaRepository<Point, Long> {

    List<Point> findAllByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from Point p where p.userId = :userId")
    List<Point> findAllByUserIdWithLock(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from Point p where p.fromType = :fromType and p.fromId = :fromId")
    Optional<Point> findByFromTypeAndFromIdWithLock(Integer fromType, Long fromId);
}
