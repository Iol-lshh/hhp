package com.lshh.hhp.orm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Table(name = "tb_point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer count;

    Long userId;

    Integer fromType;   // PointService.PointType
    Long fromId;    // PaymentId or PurchaseId
}
