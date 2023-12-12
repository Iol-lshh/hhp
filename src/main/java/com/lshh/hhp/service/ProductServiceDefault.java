package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.ProductDto;
import com.lshh.hhp.orm.entity.Product;
import com.lshh.hhp.orm.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceDefault implements ProductService{
    final ProductRepository productRepository;

    public static ProductDto toDto(Product entity){
        return new ProductDto()
                .id(entity.id())
                .name(entity.name())
                .price(entity.price());
    }
    public static Product toEntity(ProductDto dto){
        return new Product()
                .id(dto.id())
                .name(dto.name())
                .price(dto.price());
    }
    @Override
    public ResultDto<ProductDto> save(ProductDto dto) throws Exception {
        Product product;
        if(Optional.ofNullable(dto.id()).isEmpty()){
            product = toEntity(dto);
        }else{
            product = productRepository
                    .findById(dto.id())
                    .orElseThrow(Exception::new);
            product
                    .name(dto.name()==null?product.name():dto.name())
                    .price(dto.price()==null?product.price():dto.price());
        }
        product = productRepository.save(product);
        return new ResultDto<>(this.toDto(product));
    }

    @Override
    public Optional<ProductDto> find(long id) {
        return productRepository
                .findById(id)
                .map(ProductServiceDefault::toDto);
    }

    @Override
    public List<ProductDto> findAll() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductServiceDefault::toDto)
                .toList();
    }
}
