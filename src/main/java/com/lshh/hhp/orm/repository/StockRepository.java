package com.lshh.hhp.orm.repository;

import com.lshh.hhp.dto.StockDto;
import com.lshh.hhp.orm.entity.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findAllByProductId(long productId);
    List<Stock> findAllByProductIdAndPurchaseIdIsNull(long productId);
    List<Stock> findAllByProductIdAndPurchaseIdIsNull(long productId, Pageable pageable);
}
