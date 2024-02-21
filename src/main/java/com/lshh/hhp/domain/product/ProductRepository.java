package com.lshh.hhp.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findFirstByIdAndStockCntGreaterThanEqual(Long id, Integer stockCnt);

    List<Product> findTopPurchased(int pageSize, int pageNo);

    Optional<Product> findById(Long productId);

    List<Product> saveAllAndFlush(List<Product> products);

    List<Product> findAll();
}
