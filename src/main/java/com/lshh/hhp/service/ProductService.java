package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.ProductDto;
import com.lshh.hhp.dto.PurchaseDto;
import com.lshh.hhp.dto.RequestPurchaseDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ResultDto<ProductDto> save(ProductDto dto) throws Exception;

    Optional<ProductDto> find(long id);
    List<ProductDto> findAll();

    List<ProductDto> findAll(List<Long> productIdList);

    PurchaseDto convertDtoByProductPrice(RequestPurchaseDto requestDto);
}
