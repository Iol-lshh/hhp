package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.dto.OrderDto;
import com.lshh.hhp.dto.PointDto;
import com.lshh.hhp.dto.ProductDto;
import com.lshh.hhp.dto.StockDto;
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
    @Override
    @Transactional
    public Result order(long userId, long productId) throws Exception {

        // # 1. 주문 생성: 시작
        userService.find(userId).orElseThrow(Exception::new);
        Order order = new Order()
            .userId(userId)
            .state(Result.Start.ordinal());
        order = orderRepository.save(order);
        // # todo 병렬로 처리
        // ## 0. product
        ProductDto productDto = productService
            .find(productId)
            .orElseThrow(Exception::new);
        // ## 1. 재고 확인
        StockDto stockDto = stockService
            .findAllByProductId(productId)
            .stream()
            .findFirst()
            .orElseThrow(Exception::new);
        //  ## 2. 아이디 포인트 확인
        boolean isPossible = pointService
            .remain(userId) > productDto.price();
        if(!isPossible){
            throw new Exception();
        }
        // # 2. 구매 처리
        // # todo 병렬로 처리
        // ## 1. 구매 생성

        // ## 2. 아이디 포인트 차감

        // ## 3. 상품 재고 처리

        // # 3. 주문 완료: 종료
        orderRepository.save(order.state(Result.OK.ordinal()));
        return null;
    }

    @Override
    public List<OrderDto> findAll() {
        return null;
    }

    @Override
    public Optional<OrderDto> find(long id) {
        return Optional.empty();
    }

    @Override
    public List<OrderDto> findByUserId(long userId) {
        return null;
    }
}
