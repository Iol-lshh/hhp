package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.orderItem.repository.OrderItemRepository;
import com.lshh.hhp.orderItem.repository.VTopPurchasedProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Biz
public class OrderItemComponentImpl implements OrderItemComponent {

    final VTopPurchasedProductRepository vTopPurchasedProductRepository;
    final OrderItemRepository purchaseRepository;

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(rollbackFor = Exception.class)
    public List<OrderItemDto> save(List<OrderItem> purchaseList) {
        return purchaseRepository
                .saveAll(purchaseList)
                .stream()
                .map(OrderItemComponent::toDto)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrderItemDto> cancledByOrderId(long orderId) {

        List<OrderItem> list = purchaseRepository.findByOrderId(orderId)
                .stream()
                .map(purchase -> purchase.state(Result.CANCELED.ordinal()))
                .toList();

        return save(list);
    }
}
