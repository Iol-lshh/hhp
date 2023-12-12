package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.StockDto;
import com.lshh.hhp.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface StockService {
    ResultDto<StockDto> save(StockDto dto) throws Exception;

    List<StockDto> findAll();
    Optional<StockDto> find(long id);

    List<StockDto> findAllByProductId(long productId);
    List<StockDto> findAllInStockByProductId(long productId);

    default boolean isInStock(long productId){
        return findAllInStockByProductId(productId)
                .stream()
                .findFirst()
                .isPresent();
    }
    ResultDto<List<StockDto>> input(long productId, int cnt);
    ResultDto<StockDto> output(long productId, long purchaseId) throws Exception;

    boolean isAllInStock(List<Long> productIdList);
}
