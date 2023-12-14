package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PurchaseDto;
import com.lshh.hhp.dto.RequestPurchaseDto;
import com.lshh.hhp.dto.ViewPurchasedProductDto;
import com.lshh.hhp.orm.entity.Purchase;
import com.lshh.hhp.orm.repository.PurchaseRepository;
import com.lshh.hhp.orm.repository.VTopPurchasedProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class PurchaseServiceDefault implements PurchaseService{

    final PurchaseRepository purchaseRepository;
    final UserService userService;
    final PointService pointService;
    final ProductService productService;
    final VTopPurchasedProductRepository vTopPurchasedProductRepository;

    public static PurchaseDto toDto(Purchase entity){
        return new PurchaseDto()
                .id(entity.id())
                .paid(entity.paid())
                .count(entity.count())
                .productId(entity.productId())
                .userId(entity.userId())
                .orderId(entity.orderId());
    }
    public static Purchase toEntity(PurchaseDto dto){
        return new Purchase()
                .id(dto.id())
                .paid(dto.paid())
                .count(dto.count())
                .productId(dto.productId())
                .userId(dto.userId())
                .orderId(dto.orderId());
    }

    @Override
    @Transactional
    public ResultDto<List<PurchaseDto>> purchase(long userId, long orderId, List<RequestPurchaseDto> requestList) throws Exception {

        List<Purchase> purchaseList = requestList
                .stream()
                .map(request -> new Purchase()
                        .userId(userId)
                        .orderId(orderId)
                        .productId(request.getProductId())
                        .paid(productService.find(request.getProductId()).map(e->e.price()).orElse(0) * request.getCount())
                        .count(request.getCount()))
                .toList();

        List<PurchaseDto> purchaseDtoList = purchaseRepository
                .saveAllAndFlush(purchaseList)
                .stream()
                .map(PurchaseServiceDefault::toDto)
                .toList();

        pointService.purchase(purchaseDtoList);

        return new ResultDto<>(purchaseDtoList);
    }

    @Override
    public Optional<PurchaseDto> find(long id) {
        return purchaseRepository
            .findById(id)
            .map(PurchaseServiceDefault::toDto);
    }

    @Override
    public List<PurchaseDto> findAll() {
        return purchaseRepository
            .findAll()
            .stream()
            .map(PurchaseServiceDefault::toDto)
            .toList();
    }

    @Override
    public boolean isPayable(long userId, List<RequestPurchaseDto> requestList){
        return pointService.remain(userId) >= requestList
                .stream()
                .mapToInt(e->productService.convertDtoByProductPrice(e).paid()).sum();
    }

    @Override
    public List<ViewPurchasedProductDto> favorite(Integer count) {
        return vTopPurchasedProductRepository
                .findAll(Pageable.ofSize(count).withPage(0))
                .stream()
                .map(e->new ViewPurchasedProductDto()
                        .setId(e.id())
                        .setName(e.name())
                        .setPrice(e.price())
                        .setPaidCnt(e.paidCnt())
                        .setOrderByPaidCnt(e.orderByPaidCnt()))
                .toList();
    }
}
