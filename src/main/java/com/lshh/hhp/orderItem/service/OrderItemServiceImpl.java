package com.lshh.hhp.orderItem.service;

import com.lshh.hhp.common.annotation.Service;
import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.orderItem.repository.OrderItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {

    final OrderItemRepository orderItemRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrderItem> save(List<OrderItem> purchaseList) {
        return orderItemRepository
                .saveAllAndFlush(purchaseList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrderItem> canceldByOrderId(long orderId) {

        List<OrderItem> list = orderItemRepository.findByOrderId(orderId)
                .stream().map(orderItem -> orderItem.setState(Result.CANCELED.ordinal())).toList();

        return save(list);
    }
}
