package com.lshh.hhp.infra.payment;

import com.lshh.hhp.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserId(long userId);
}
