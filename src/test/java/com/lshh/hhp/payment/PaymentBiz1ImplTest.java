package com.lshh.hhp.payment;

import com.lshh.hhp.payment.PaymentDto;
import com.lshh.hhp.payment.PaymentBiz;
import com.lshh.hhp.point.PointBiz;
import com.lshh.hhp.payment.PaymentBiz1Impl;
import com.lshh.hhp.user.UserBiz;
import com.lshh.hhp.point.PointDto;
import com.lshh.hhp.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class PaymentBiz1ImplTest {

    @InjectMocks
    PaymentBiz1Impl paymentService;

    @Mock
    UserBiz userComponent;
    
    @Mock
    PaymentBiz paymentComponent;
    
    @Mock
    PointBiz pointComponent;

    long userId;
    int toNeed;
    PaymentDto mockedPaymentDto;
    @BeforeEach
    void setUp(){
        this.userId = 1L;
        this.toNeed = 10;
        this.mockedPaymentDto = new PaymentDto().id(1L).into(toNeed).userId(userId);
    }
    @Test
    void testExchange_successCase() throws Exception {
        // Arrange
        when(userComponent.find(userId)).thenReturn(Optional.of(new UserDto()));
        when(paymentComponent.create(userId, toNeed)).thenReturn(mockedPaymentDto);
        doReturn(new PointDto()).when(pointComponent).pay(mockedPaymentDto);
        
        // Act
        PaymentDto paymentDtoResult = paymentService.exchange(userId, toNeed);
        
        // Assert
        assertEquals(mockedPaymentDto.id(), paymentDtoResult.id());
        assertEquals(mockedPaymentDto.into(), paymentDtoResult.into());
        assertEquals(mockedPaymentDto.userId(), paymentDtoResult.userId());
        verify(userComponent, times(1)).find(userId);
        verify(paymentComponent, times(1)).create(userId, toNeed);
        verify(pointComponent, times(1)).pay(paymentDtoResult);
    }

    @Test
    void testExchange_failedCase() {
        // Arrange
        when(userComponent.find(userId)).thenReturn(Optional.empty());
        
        // Act and Assert
        assertThrows(Exception.class, () -> paymentService.exchange(userId, toNeed));
        verify(userComponent, times(1)).find(userId);
        verify(paymentComponent, times(0)).create(userId, toNeed);
        verify(pointComponent, times(0)).pay(mockedPaymentDto);
    }
}