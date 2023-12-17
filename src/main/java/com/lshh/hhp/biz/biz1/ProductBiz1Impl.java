package com.lshh.hhp.biz.biz1;

import com.lshh.hhp.dto.origin.ProductDto;
import com.lshh.hhp.biz.base.ProductBiz;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductBiz1Impl implements ProductBiz1 {
    final ProductBiz productComponent;

    @Override
    public List<ProductDto> findAll() {
        return productComponent
                .findAll();
    }
}
