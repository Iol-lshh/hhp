package com.lshh.hhp.order.service;

import com.lshh.hhp.common.Response.Result;
import com.lshh.hhp.order.Order;
import com.lshh.hhp.order.dto.OrderDto;

import java.util.List;
import java.util.Optional;

// base biz
public interface OrderComponent {



    List<Order> findByUserId(long userId);

    Optional<Order> find(long orderId);
    Order start(long userId);
    void setState(long orderId, Result state) throws Exception;

}
