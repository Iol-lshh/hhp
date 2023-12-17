package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.origin.PurchaseDto;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.orm.entity.Purchase;
import com.lshh.hhp.orm.repository.PurchaseRepository;
import com.lshh.hhp.orm.repository.VTopPurchasedProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class PurchaseBizImpl implements PurchaseBiz {

    final VTopPurchasedProductRepository vTopPurchasedProductRepository;
    final PurchaseRepository purchaseRepository;

    @Override
    public List<ViewPurchasedProductDto> favorite(Integer count) {
        return vTopPurchasedProductRepository
                .findAll(Pageable.ofSize(count).withPage(0))
                .stream()
                .map(e->new ViewPurchasedProductDto()
                        .setId(e.id())
                        .setName(e.name())
                        .setPrice(e.price())
                        .setPaidCnt(e.paidCnt())
                        .setOrderByPaidCnt(e.orderByPaidCnt()))
                .toList();
    }

    @Override
    public List<PurchaseDto> save(List<Purchase> purchaseList) {
        return purchaseRepository
                .saveAll(purchaseList)
                .stream()
                .map(PurchaseBiz::toDto)
                .toList();
    }
}
