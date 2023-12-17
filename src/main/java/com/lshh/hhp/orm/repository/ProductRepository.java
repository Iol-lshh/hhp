package com.lshh.hhp.orm.repository;

import com.lshh.hhp.orm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findFirstByIdAndStockCntGreaterThan(Long id, Integer stockCnt);
}
