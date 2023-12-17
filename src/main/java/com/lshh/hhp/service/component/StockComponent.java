package com.lshh.hhp.service.component;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PurchaseDto;
import com.lshh.hhp.dto.RequestPurchaseDto;
import com.lshh.hhp.dto.StockDto;

import java.util.List;
import java.util.Optional;

public interface StockComponent {
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
    default boolean isAllInStock(List<RequestPurchaseDto> requestList){
        boolean result = true;
        for(RequestPurchaseDto toPurchase:requestList){
            if(findAllInStockByProductId(toPurchase.getProductId()).size() < toPurchase.getCount()) {
                result = false;
            }
        }
        return result;
    }
    ResultDto<List<StockDto>> input(long productId, int cnt);
    ResultDto<List<StockDto>> output(List<PurchaseDto> purchaseList) throws Exception;
}
