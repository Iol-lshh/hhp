package com.lshh.hhp.payment.repository;

import com.lshh.hhp.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
