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
public class PointSource {
    @Id
    Long id;
    String name;
    Integer count;
}
