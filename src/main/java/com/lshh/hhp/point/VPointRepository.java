package com.lshh.hhp.point;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VPointRepository extends JpaRepository<VPoint, Long> {

    Optional<VPoint> findByUserId(long userId);
}
