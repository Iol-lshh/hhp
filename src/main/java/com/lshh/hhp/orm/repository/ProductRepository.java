package com.lshh.hhp.orm.repository;

import com.lshh.hhp.orm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findFirstByIdAndStockCntGreaterThanEqual(Long id, Integer stockCnt);
}
