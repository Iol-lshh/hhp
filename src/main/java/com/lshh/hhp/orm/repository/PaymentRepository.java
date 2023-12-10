package com.lshh.hhp.orm.repository;

import com.lshh.hhp.orm.entity.Payment;
import com.lshh.hhp.orm.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
