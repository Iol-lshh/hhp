package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.ProductDto;
import com.lshh.hhp.dto.PurchaseDto;
import com.lshh.hhp.orm.entity.Purchase;
import com.lshh.hhp.orm.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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

    public static PurchaseDto toDto(Purchase entity){
        return new PurchaseDto()
                .id(entity.id())
                .paid(entity.paid())
                .productId(entity.productId())
                .userId(entity.userId())
                .orderId(entity.orderId());
    }
    public static Purchase toEntity(PurchaseDto dto){
        return new Purchase()
                .id(dto.id())
                .paid(dto.paid())
                .productId(dto.productId())
                .userId(dto.userId())
                .orderId(dto.orderId());
    }

    @Override
    @Transactional
    public ResultDto<PurchaseDto> purchase(long userId, long productId, long orderId) throws Exception {
        userService.find(userId).orElseThrow(Exception::new);
        ProductDto productDto = productService.find(productId).orElseThrow(Exception::new);

        Purchase purchase = new Purchase()
                .userId(userId)
                .productId(productId)
                .orderId(orderId)
                .paid(productDto.price());

        purchase = purchaseRepository.save(purchase);
        PurchaseDto purchaseDto = PurchaseServiceDefault.toDto(purchase);
        pointService.purchase(purchaseDto);

        return new ResultDto<>(purchaseDto);
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
}
