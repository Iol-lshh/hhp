package com.lshh.hhp.biz.biz1;

import com.lshh.hhp.biz.base.PointBiz;
import com.lshh.hhp.biz.base.ProductBiz;
import com.lshh.hhp.biz.base.PurchaseBiz;
import com.lshh.hhp.dto.origin.ProductDto;
import com.lshh.hhp.dto.origin.PurchaseDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PurchaseBiz1ImplTest {

    @Mock
    PointBiz pointComponent;
    @Mock
    PurchaseBiz purchaseComponent;
    @Mock
    ProductBiz productComponent;

    @InjectMocks
    PurchaseBiz1Impl purchaseService;

    PurchaseDto purchase;
    RequestPurchaseDto requestPurchaseDto;

    List<RequestPurchaseDto> requestPurchaseList;

    @BeforeEach
    public void setup() {
        purchase = new PurchaseDto()
                .userId(1L)
                .orderId(1L)
                .productId(1L)
                .paid(100)
                .count(1);

        requestPurchaseDto = new RequestPurchaseDto()
                .setProductId(purchase.productId())
                .setCount(purchase.count());

        this.requestPurchaseList = Arrays.asList(this.requestPurchaseDto);
    }

    // # isPayable
    @Test
    @DisplayName("지불 가능")
    void isPayable_withEnoughPoints_shouldReturnTrue() {

        when(pointComponent.remain(purchase.userId())).thenReturn(100);
        when(productComponent.findPrice(requestPurchaseDto.getProductId())).thenReturn(50);

        // Act
        boolean result = purchaseService.isPayable(purchase.userId(), requestPurchaseList);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("지불 불가")
    void isPayable_withNotEnoughPoints_shouldReturnFalse() {

        when(pointComponent.remain(purchase.userId())).thenReturn(50);
        when(productComponent.findPrice(requestPurchaseDto.getProductId())).thenReturn(100);

        // Act
        boolean result = purchaseService.isPayable(purchase.userId(), requestPurchaseList);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("구매 성공")
    void purchase() throws Exception {
        when(purchaseComponent.save(any())).thenReturn(Arrays.asList(purchase));
        when(productComponent.find(anyLong())).thenReturn(java.util.Optional.of(new ProductDto().price(100)));

        // Act
        List<PurchaseDto> result = purchaseService.purchase(purchase.userId(), purchase.orderId(), Collections.singletonList(requestPurchaseDto));

        // Assert
        verify(purchaseComponent).save(any());
        verify(pointComponent).purchase(any());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(purchase, result.get(0));
      }
}