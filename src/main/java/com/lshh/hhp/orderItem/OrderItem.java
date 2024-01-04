package com.lshh.hhp.orderItem;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Getter
@Accessors(fluent = true)
@Table(name = "tb_order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer paid;
    Integer count;

    Long userId;
    Long productId;
    Long orderId;

    Integer state;

    public OrderItem setPriceTag(Integer price){
        this.paid = price * count;
        return this;
    }
    public OrderItem setState(Integer state) {
        this.state = state;
        return this;
    }

    public OrderItemDto toDto(){
        return new OrderItemDto()
                .id(this.id())
                .paid(this.paid())
                .count(this.count())
                .productId(this.productId())
                .userId(this.userId())
                .orderId(this.orderId())
                .state(Response.Result.of(this.state()));
    }

    static public OrderItem toEntity(OrderItemDto dto){
        OrderItem orderItem = new OrderItem();
        orderItem.id = dto.id();
        orderItem.paid = dto.paid();
        orderItem.count = dto.count();
        orderItem.productId = dto.productId();
        orderItem.userId = dto.userId();
        orderItem.orderId = dto.orderId();
        orderItem.state = dto.state().ordinal();

        return orderItem;
    }

    static public OrderItem createNewOrderItemWithNoPriceTag(Long userId, Long orderId, Long productId, int count){
        OrderItem newOne = new OrderItem();
        newOne.userId = userId;
        newOne.orderId = orderId;
        newOne.productId = productId;
        newOne.count = count;
        newOne.state = Response.Result.OK.ordinal();
        return newOne;
    }

    static public List<OrderItem> createNewOrderItemsWithNoPriceTag(Long userId, Long orderId, List<RequestPurchaseDto> requestList){
        return requestList.stream()
            .map(request -> createNewOrderItemWithNoPriceTag(userId, orderId, request.getProductId(), request.getCount()))
            .toList();
    }
}
