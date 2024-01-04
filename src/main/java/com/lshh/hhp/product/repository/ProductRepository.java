package com.lshh.hhp.product.repository;

import com.lshh.hhp.product.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findFirstByIdAndStockCntGreaterThanEqual(Long id, Integer stockCnt);


}
