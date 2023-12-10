package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Result save(ProductDto dto);

    Optional<ProductDto> find(long id);
    List<ProductDto> findAll();
}
