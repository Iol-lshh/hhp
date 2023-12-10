package com.lshh.hhp.orm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    Long id;
    String name;
}
