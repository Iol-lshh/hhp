package com.lshh.hhp.orm.repository;

import com.lshh.hhp.orm.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByOrderId(long orderId);
}
