package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response;
import com.lshh.hhp.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Response.Result save(ProductDto dto);

    Optional<ProductDto> find();
    List<ProductDto> findAll();
}
