package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.*;
import com.lshh.hhp.orm.entity.Order;
import com.lshh.hhp.orm.repository.OrderRepository;
import com.lshh.hhp.service.component.OrderComponent;
import com.lshh.hhp.service.component.PointComponent;
import com.lshh.hhp.service.component.PurchaseComponent;
import com.lshh.hhp.service.component.UserComponent;
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


    final UserService userService;
    final PurchaseService purchaseService;
    final PointService pointService;
    final ProductService productService;
    final StockService stockService;

    public static OrderDto toDto(Order entity){
        return new OrderDto()
                .id(entity.id())
                .state(Result.of(entity.state()))
                .userId(entity.userId());
    }
    public static Order toEntity(OrderDto dto){
        return new Order()
                .id(dto.id())
                .state(dto.state().ordinal())
                .userId(dto.userId());
    }

    @Override
    @Transactional
    public ResultDto<OrderDto> order(long userId, List<RequestPurchaseDto> purchaseRequestList) throws Exception {
        // # 0. user 확인
        userService.find(userId).orElseThrow(Exception::new);
        // # 1. 주문 생성: 시작
        Order order = new Order()
            .userId(userId)
            .state(Result.Start.ordinal());

        order = orderRepository.save(order);
        try {


            // ## 0. 상품 확인
            if (purchaseRequestList.stream()
                    .filter(request -> productService.find(request.getProductId()).isPresent())
                    .count() != purchaseRequestList.size()
            ) {
                throw new Exception("잘못된 상품");
            }

            // ## 1. 재고 확인
            // product.id, count(stock.id) from product join stock group by product.id
            if (!stockService.isAllInStock(purchaseRequestList)) {
                throw new Exception("재고 부족");
            }
            //  ## 2. 아이디 포인트 확인
            if (!purchaseService.isPayable(userId, purchaseRequestList)) {
                throw new Exception("포인트 부족");
            }
            // # 2. 구매 처리
            // ## 1. 구매 생성
            List<PurchaseDto> purchaseList = purchaseService.purchase(userId, order.id(), purchaseRequestList).getValue();
            // ## 2. 상품 재고 처리
            stockService.output(purchaseList);
            // # 3. 주문 완료: 종료
            order = orderRepository.save(order.state(Result.OK.ordinal()));
            return new ResultDto<>(Result.OK, toDto(order));
        }catch (Exception err){
            order.state(Result.FAIL.ordinal());
            orderRepository.save(order);
            throw err;
        }
    }

    @Override
    public List<OrderDto> findAll() {
        return orderRepository
            .findAll()
            .stream()
            .map(OrderServiceDefault::toDto)
            .toList();
    }

    @Override
    public Optional<OrderDto> find(long id) {
        return orderRepository
            .findById(id)
            .map(OrderServiceDefault::toDto);
    }

    @Override
    public List<OrderDto> findByUserId(long userId) {
        return orderRepository
            .findByUserId(userId)
            .stream()
            .map(OrderServiceDefault::toDto)
            .toList();
    }
}
