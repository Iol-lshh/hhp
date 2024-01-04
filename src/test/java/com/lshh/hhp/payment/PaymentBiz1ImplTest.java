package com.lshh.hhp.payment;

import com.lshh.hhp.payment.dto.PaymentDto;
import com.lshh.hhp.payment.service.PaymentService;
import com.lshh.hhp.payment.service.Payment1ServiceImpl;
import com.lshh.hhp.point.Point;
import com.lshh.hhp.point.service.PointService;
import com.lshh.hhp.user.User;
import com.lshh.hhp.user.service.UserService;
import com.lshh.hhp.point.dto.PointDto;
import com.lshh.hhp.user.dto.UserDto;
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
    Payment1ServiceImpl paymentService;

    @Mock
    UserService userComponent;
    
    @Mock
    PaymentService paymentComponent;
    
    @Mock
    PointService pointComponent;

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
        Payment mockedPayment = Payment.toEntity(mockedPaymentDto);
        // Arrange
        when(userComponent.find(userId)).thenReturn(Optional.of(new User()));
        when(paymentComponent.create(userId, toNeed)).thenReturn(mockedPayment);
        doReturn(new Point()).when(pointComponent).add(mockedPayment);
        
        // Act
        PaymentDto paymentDtoResult = paymentService.exchange(userId, toNeed);
        
        // Assert
        assertEquals(mockedPaymentDto.id(), paymentDtoResult.id());
        assertEquals(mockedPaymentDto.into(), paymentDtoResult.into());
        assertEquals(mockedPaymentDto.userId(), paymentDtoResult.userId());
        verify(userComponent, times(1)).find(userId);
        verify(paymentComponent, times(1)).create(userId, toNeed);
        verify(pointComponent, times(1)).add(mockedPayment);
    }

    @Test
    void testExchange_failedCase() {
        Payment mockedPayment = Payment.toEntity(mockedPaymentDto);
        // Arrange
        when(userComponent.find(userId)).thenReturn(Optional.empty());
        
        // Act and Assert
        assertThrows(Exception.class, () -> paymentService.exchange(userId, toNeed));
        verify(userComponent, times(1)).find(userId);
        verify(paymentComponent, times(0)).create(userId, toNeed);
        verify(pointComponent, times(0)).add(mockedPayment);
    }
}