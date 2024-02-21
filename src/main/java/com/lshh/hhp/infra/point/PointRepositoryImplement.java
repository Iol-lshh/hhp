package com.lshh.hhp.infra.point;

import com.lshh.hhp.domain.point.Point;
import com.lshh.hhp.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImplement implements PointRepository {

    final PointJpaRepository pointJpaRepository;

    @Override
    public List<Point> findAllByUserId(Long userId) {
        return pointJpaRepository.findAllByUserId(userId);
    }

    @Override
    public List<Point> findAllByUserIdWithLock(Long userId) {
        return pointJpaRepository.findAllByUserIdWithLock(userId);
    }

    @Override
    public Optional<Point> findByFromTypeAndFromIdWithLock(Integer fromType, Long fromId) {
        return pointJpaRepository.findByFromTypeAndFromIdWithLock(fromType, fromId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        pointJpaRepository.deleteByUserId(userId);
    }

    @Override
    public List<Point> saveAllAndFlush(List<Point> pointList) {
        return pointJpaRepository.saveAllAndFlush(pointList);
    }

    @Override
    public Point save(Point point) {
        return pointJpaRepository.save(point);
    }
}
