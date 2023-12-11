package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.PointDto;
import com.lshh.hhp.dto.ProductDto;
import com.lshh.hhp.dto.PurchaseDto;
import com.lshh.hhp.orm.entity.Product;
import com.lshh.hhp.orm.entity.Purchase;
import com.lshh.hhp.orm.repository.ProductRepository;
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

    public PurchaseDto toDto(Purchase entity){
        return new PurchaseDto()
                .id(entity.id())
                .paid(entity.paid())
                .productId(entity.productId())
                .userId(entity.userId());
    }
    public Purchase toEntity(PurchaseDto dto){
        return new Purchase()
                .id(dto.id())
                .paid(dto.paid())
                .productId(dto.productId())
                .userId(dto.userId());
    }

    @Override
    @Transactional
    public ResultDto<PurchaseDto> purchase(long userId, long productId) throws Exception {
        userService.find(userId).orElseThrow(Exception::new);
        ProductDto productDto = productService.find(productId).orElseThrow(Exception::new);

        Purchase purchase = new Purchase()
                .userId(userId)
                .productId(productId)
                .paid(productDto.price());

        purchase = purchaseRepository.save(purchase);
        PurchaseDto purchaseDto = this.toDto(purchase);
        pointService.purchase(purchaseDto);

        return new ResultDto<>(Result.OK, purchaseDto);
    }

    @Override
    public Optional<PurchaseDto> find(long id) {
        return purchaseRepository
            .findById(id)
            .map(this::toDto);
    }

    @Override
    public List<PurchaseDto> findAll() {
        return purchaseRepository
            .findAll()
            .stream()
            .map(this::toDto)
            .toList();
    }
}
