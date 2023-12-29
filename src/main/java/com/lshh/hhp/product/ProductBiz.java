package com.lshh.hhp.product;

import com.lshh.hhp.purchase.PurchaseDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;

import java.util.List;
import java.util.Optional;

public interface ProductBiz {
    static ProductDto toDto(Product entity){
        return new ProductDto()
                .id(entity.id())
                .name(entity.name())
                .price(entity.price())
                .stockCnt(entity.stockCnt());
    }
    static Product toEntity(ProductDto dto){
        return new Product()
                .id(dto.id())
                .name(dto.name())
                .price(dto.price())
                .stockCnt(dto.stockCnt());
    }

    boolean validate(List<RequestPurchaseDto> purchaseRequestList);

    boolean isInStock(List<RequestPurchaseDto> purchaseRequestList);

    int findPrice(Long productId);

    List<ProductDto> unstore(List<RequestPurchaseDto> purchaseList) throws Exception;

    List<ProductDto> restore(List<PurchaseDto> purchaseList) throws Exception;

    List<ProductDto> findAll();

    Optional<ProductDto> find(Long productId);

}
