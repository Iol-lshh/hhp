package com.lshh.hhp.orm.repository;

import com.lshh.hhp.orm.entity.VPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface VPointRepository extends JpaRepository<VPoint, Long> {

    Optional<VPoint> findByUserId(long userId);
}
