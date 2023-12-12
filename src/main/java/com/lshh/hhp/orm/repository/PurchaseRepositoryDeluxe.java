package com.lshh.hhp.orm.repository;

import com.lshh.hhp.dto.ViewOrderDto;
import com.lshh.hhp.dto.ViewPurchaseDto;
import com.lshh.hhp.orm.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PurchaseRepositoryDeluxe {
    final OrderRepository orderRepository;
    final PurchaseRepository purchaseRepository;
    final JPAQueryFactory queryFactory;
    @Transactional
    public List<ViewOrderDto> findViewByUserId(long userId){

        QOrder qOrder = QOrder.order;
        QPurchase qPurchase = QPurchase.purchase;
        QProduct qProduct = QProduct.product;

        List<ViewPurchaseDto> viewPurchaseDtos = queryFactory.select(
                Projections.constructor(ViewPurchaseDto.class,
                        Expressions.asNumber(userId).as("userId"),
                        qOrder.id.as("orderId"),
                        qProduct.id.as("productId"),
                        qPurchase.id.as("purchaseId"),
                        qProduct.name.as("productName"),
                        qProduct.price.as("productPrice"),
                        qPurchase.paid.as("purchasePaid")
                )
            )
            .from(qOrder)
            .leftJoin(qPurchase)
                .on(qPurchase.orderId.eq(qOrder.id))
            .leftJoin(qProduct)
                .on(qProduct.id.eq(qPurchase.productId))
            .where(qOrder.userId.eq(userId))
            .fetch();

        //todo
        return null;
    }
}
