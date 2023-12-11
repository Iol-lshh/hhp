package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.*;
import com.lshh.hhp.orm.entity.Order;
import com.lshh.hhp.orm.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceDefault implements OrderService{

    final OrderRepository orderRepository;
    final UserService userService;
    final PurchaseService purchaseService;
    final PointService pointService;
    final ProductService productService;
    final StockService stockService;

    public OrderDto toDto(Order entity){
        return new OrderDto()
                .id(entity.id())
                .state(Result.of(entity.state()))
                .userId(entity.userId());
    }
    public Order toEntity(OrderDto dto){
        return new Order()
                .id(dto.id())
                .state(dto.state().ordinal())
                .userId(dto.userId());
    }

    @Override
    @Transactional
    public ResultDto<OrderDto> order(long userId, long productId) throws Exception {
        // # 0. user 확인
        userService.find(userId).orElseThrow(Exception::new);
        // # 1. 주문 생성: 시작
        Order order = new Order()
            .userId(userId)
            .state(Result.Start.ordinal());
        order = orderRepository.save(order);
        // ## 0. 상품 확인
        ProductDto productDto = productService
            .find(productId)
            .orElseThrow(Exception::new);
        // ## 1. 재고 확인
        if(!stockService.isInStock(productId)){
            throw new Exception("재고 부족");
        }
        //  ## 2. 아이디 포인트 확인
        if(!pointService.isPayable(userId, productDto.price())){
            throw new Exception("포인트 부족");
        }
        // # 2. 구매 처리
        // ## 1. 구매 생성
        PurchaseDto purchaseDto = purchaseService.purchase(userId, productId).getValue();
        // ## 2. 상품 재고 처리
        stockService.output(productId, purchaseDto.id());
        // # 3. 주문 완료: 종료
        order = orderRepository.save(order.state(Result.OK.ordinal()));

        return new ResultDto<>(Result.OK, this.toDto(order));
    }

    @Override
    public List<OrderDto> findAll() {
        return orderRepository
            .findAll()
            .stream()
            .map(this::toDto)
            .toList();
    }

    @Override
    public Optional<OrderDto> find(long id) {
        return orderRepository
            .findById(id)
            .map(this::toDto);
    }

    @Override
    public List<OrderDto> findByUserId(long userId) {
        return orderRepository
            .findByUserId(userId)
            .stream()
            .map(this::toDto)
            .toList();
    }
}
