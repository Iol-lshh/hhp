package com.lshh.hhp.biz.biz2;

import com.lshh.hhp.dto.ResultDto;
import com.lshh.hhp.dto.origin.OrderDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.biz.base.*;
import com.lshh.hhp.biz.biz1.PurchaseBiz1;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderBiz2Impl implements OrderBiz2 {

    // 규칙. 작은 숫자의 biz1 만을 확장 가능하다.
    final PurchaseBiz1 purchaseComponent;

    final OrderBiz orderComponent;
    final UserBiz userComponent;
    final PointBiz pointComponent;
    final ProductBiz productComponent;

    @Override
    @Transactional
    public ResultDto<OrderDto> order(long userId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {
        // # 0. user 확인
        userComponent.find(userId).orElseThrow(Exception::new);
        // # 1. 주문 생성: 시작
        OrderDto order = orderComponent.start(userId);
        try {
            // ## 0. 상품 확인
            if(!productComponent.validate(purchaseRequestList)){
                throw new Exception("잘못된 상품");
            }
            // ## 1. 재고 확인
            // product.id, count(stock.id) from product join stock group by product.id
            if (!productComponent.isInStock(purchaseRequestList)) {
                throw new Exception("재고 부족");
            }
            //  ## 2. 아이디 포인트 확인
            if (isPayable(userId, purchaseRequestList)) {
                throw new Exception("포인트 부족");
            }
            // # 2. 구매 처리
            // ## 1. 구매 생성
            purchaseComponent.purchase(userId, order.id(), purchaseRequestList);
            // ## 2. 상품 재고 처리
            productComponent.unstore(purchaseRequestList);
            // # 3. 주문 완료: 종료
            return new ResultDto<>(orderComponent.success(order));
        }catch (Exception err){
            orderComponent.fail(order);
            throw err;
        }
    }

    @Override
    public boolean isPayable(long userId, List<RequestPurchaseDto> requestList){
        return pointComponent.remain(userId) >= requestList
                .stream()
                .mapToInt(e->productComponent.findPrice(e.getProductId()))
                .sum();
    }

    @Override
    public List<OrderDto> findByUserId(long userId) {
        return orderComponent
            .findByUserId(userId);
    }
}
