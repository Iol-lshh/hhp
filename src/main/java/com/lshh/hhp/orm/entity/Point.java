package com.lshh.hhp.orm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class Point {
    @Id
    Long id;
    String name;
    Integer count;

    Long userId;

    Integer fromType;   // PointService.PointType
    Long fromId;    // PaymentId or PurchaseId
}
