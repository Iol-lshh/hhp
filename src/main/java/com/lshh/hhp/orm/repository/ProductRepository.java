package com.lshh.hhp.orm.repository;

import com.lshh.hhp.orm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
