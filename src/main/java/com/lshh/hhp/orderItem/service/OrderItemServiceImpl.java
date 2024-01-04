package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.common.Service;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.dto.view.ViewPurchasedProductDto;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.orderItem.repository.OrderItemRepository;
import com.lshh.hhp.orderItem.repository.VTopPurchasedProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {

    final VTopPurchasedProductRepository vTopPurchasedProductRepository;
    final OrderItemRepository orderItemRepository;

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
    public List<OrderItem> save(List<OrderItem> purchaseList) {
        return orderItemRepository
                .saveAll(purchaseList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrderItem> canceldByOrderId(long orderId) {

        List<OrderItem> list = orderItemRepository.findByOrderId(orderId)
                .stream().map(orderItem -> orderItem.setState(Result.CANCELED.ordinal())).toList();

        return save(list);
    }
}
