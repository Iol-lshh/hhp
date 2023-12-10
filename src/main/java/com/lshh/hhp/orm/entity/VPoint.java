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
public class VPoint {
    @Id
    Long userId;
    Integer remain;
}