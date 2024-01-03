package com.lshh.hhp.order;

import com.lshh.hhp.common.Response;
import com.lshh.hhp.order.dto.OrderDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.aspectj.weaver.ast.Or;

@Entity
@Getter
@Accessors(fluent = true)
@Table(name = "tb_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long userId;
    Integer state;

    static public Order createNewOrder(Long userId){
        Order order = new Order();
        order.userId = userId;
        order.state = Response.Result.START.ordinal();
        return order;
    }

    public void setState(Response.Result state) throws Exception{
        this.state = state.ordinal();
    }

    public OrderDto toDto(){
        return new OrderDto()
                .id(this.id())
                .state(Response.Result.of(this.state()))
                .userId(this.userId());
    }
    static public Order toEntity(OrderDto dto){
        Order entity = new Order();
        entity.id = dto.id();
        entity.userId = dto.userId();
        entity.state = dto.state().ordinal();
        return entity;
    }
}
