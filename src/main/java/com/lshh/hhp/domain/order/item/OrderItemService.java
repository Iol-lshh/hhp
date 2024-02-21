package com.lshh.hhp.domain.order.item;

import com.lshh.hhp.common.annotation.Service;
import com.lshh.hhp.common.Response.Result;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderItemService {

    final OrderItemRepository orderItemRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<OrderItem> save(List<OrderItem> purchaseList) {
        return orderItemRepository
                .saveAllAndFlush(purchaseList);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<OrderItem> canceldByOrderId(long orderId) {

        List<OrderItem> list = orderItemRepository.findByOrderId(orderId)
                .stream().map(orderItem -> orderItem.setState(Result.CANCELED.ordinal())).toList();

        return save(list);
    }
}
