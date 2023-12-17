package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.*;
import com.lshh.hhp.orm.entity.Order;
import com.lshh.hhp.orm.repository.OrderRepository;
import com.lshh.hhp.service.component.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceDefault implements OrderService{

    final OrderComponent orderComponent;
    final UserComponent userComponent;
    final PurchaseComponent purchaseComponent;
    final PointComponent pointComponent;
    final ProductComponent productComponent;
    final StockComponent stockComponent;

    @Override
    @Transactional
    public ResultDto<OrderDto> order(long userId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {
        // # 0. user 확인
        userComponent.find(userId).orElseThrow(Exception::new);
        // # 1. 주문 생성: 시작
        OrderDto order = new OrderDto()
            .userId(userId)
            .state(Result.Start);

        order = orderComponent.save(order);
        try {


            // ## 0. 상품 확인
            if (purchaseRequestList.stream()
                    .filter(request -> productComponent.find(request.getProductId()).isPresent())
                    .count() != purchaseRequestList.size()
            ) {
                throw new Exception("잘못된 상품");
            }

            // ## 1. 재고 확인
            // product.id, count(stock.id) from product join stock group by product.id
            if (!stockComponent.isAllInStock(purchaseRequestList)) {
                throw new Exception("재고 부족");
            }
            //  ## 2. 아이디 포인트 확인
            if (!purchaseComponent.isPayable(userId, purchaseRequestList)) {
                throw new Exception("포인트 부족");
            }
            // # 2. 구매 처리
            // ## 1. 구매 생성
            List<PurchaseDto> purchaseList = purchaseComponent.purchase(userId, order.id(), purchaseRequestList).getValue();
            // ## 2. 상품 재고 처리
            stockComponent.output(purchaseList);
            // # 3. 주문 완료: 종료
            order = orderComponent.save(order.state(Result.OK));
            return new ResultDto<>(order);
        }catch (Exception err){
            order.state(Result.FAIL);
            orderComponent.save(order);
            throw err;
        }
    }

    @Override
    public List<OrderDto> findAll() {
        return orderComponent.findAll();
    }

    @Override
    public Optional<OrderDto> find(long id) {
        return orderComponent.findById(id);
    }

    @Override
    public List<OrderDto> findByUserId(long userId) {
        return orderComponent.findByUserId(userId);
    }
}
