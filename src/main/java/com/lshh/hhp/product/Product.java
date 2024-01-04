package com.lshh.hhp.product;

import com.lshh.hhp.product.dto.ProductDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Accessors(chain = true, fluent = true)
@Table(name = "tb_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Integer price;
    Integer stockCnt;

    protected Product price(int price){
        this.price = price;
        return this;
    }
    public Product conduct(int cnt){
        this.stockCnt += cnt;
        return this;
    }
    public Product deduct(int cnt){
        this.stockCnt -= cnt;
        return this;
    }
    public ProductDto toDto(){
        return new ProductDto()
            .id(this.id)
            .name(this.name)
            .price(this.price)
            .stockCnt(this.stockCnt);
    }
    public static Product toEntity(ProductDto dto){
        Product entity = new Product();
        entity.id = dto.id();
        entity.name = dto.name();
        entity.price = dto.price();
        entity.stockCnt = dto.stockCnt();
        return entity;
    }
}
