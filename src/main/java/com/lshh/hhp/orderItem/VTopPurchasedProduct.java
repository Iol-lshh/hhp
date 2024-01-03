package com.lshh.hhp.orderItem;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Table(name = "v_top_purchased_product")
public class VTopPurchasedProduct {
    @Id
    Long id;
    String name;
    Integer price;
    Integer paidCnt;
    Integer orderByPaidCnt;
}
