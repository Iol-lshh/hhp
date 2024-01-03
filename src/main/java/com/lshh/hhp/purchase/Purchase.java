package com.lshh.hhp.purchase;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Table(name = "tb_purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer paid;
    Integer count;

    Long userId;
    Long productId;
    Long orderId;

    Integer state;
}
