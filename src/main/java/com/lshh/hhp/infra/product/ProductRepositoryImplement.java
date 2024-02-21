package com.lshh.hhp.infra.product;

import com.lshh.hhp.domain.product.ProductRepository;
import com.lshh.hhp.domain.product.Product;
import com.lshh.hhp.infra.point.PointJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProductRepositoryImplement implements ProductRepository {
    final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<Product> findFirstByIdAndStockCntGreaterThanEqual(Long id, Integer stockCnt) {
        return productJpaRepository.findFirstByIdAndStockCntGreaterThanEqual(id, stockCnt);
    }

    @Override
    public List<Product> findTopPurchased(int pageSize, int pageNo) {
        return productJpaRepository.findTopPurchased(pageSize, pageNo);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public List<Product> saveAllAndFlush(List<Product> products) {
        return productJpaRepository.saveAllAndFlush(products);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }
}
