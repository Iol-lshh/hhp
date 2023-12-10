package com.lshh.hhp.orm.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class Stock {
    @Id
    Long id;
    String name;

    Long productId;
    @Nullable
    Long purchaseId;
}
