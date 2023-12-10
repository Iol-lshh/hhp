package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.dto.ProductDto;
import com.lshh.hhp.orm.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceDefault implements ProductService{

    public ProductDto toDto(Product entity){
        return new ProductDto()
                .id(entity.id())
                .name(entity.name())
                .price(entity.price());
    }
    public Product toEntity(ProductDto dto){
        return new Product()
                .id(dto.id())
                .name(dto.name())
                .price(dto.price());
    }
    @Override
    public Result save(ProductDto dto) {
        return null;
    }

    @Override
    public Optional<ProductDto> find(long id) {
        return Optional.empty();
    }

    @Override
    public List<ProductDto> findAll() {
        return null;
    }
}
