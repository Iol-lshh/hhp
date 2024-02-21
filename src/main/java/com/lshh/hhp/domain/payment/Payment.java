package com.lshh.hhp.domain.payment;

import com.lshh.hhp.domain.payment.dto.PaymentDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Accessors(fluent = true)
@Table(name = "tb_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "exchanged")
    Integer into;
    Long userId;

    public static Payment createNewPayment(long userId, int into){
        Payment newOne = new Payment();
        newOne.userId = userId;
        newOne.into = into;
        return newOne;
    }

    public PaymentDto toDto(){
        return new PaymentDto()
                .id(this.id())
                .into(this.into())
                .userId(this.userId());
    }
    public static Payment toEntity(PaymentDto dto){
        Payment entity = new Payment();
        entity.id = dto.id();
        entity.into = dto.into();
        entity.userId = dto.userId();
        return entity;
    }
}
