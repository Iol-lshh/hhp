package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.dto.PointDto;
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
    public Result purchase(long userId, int paid) throws Exception {
        userService.find(userId).orElseThrow(Exception::new);

        Purchase purchase = new Purchase()
                .userId(userId)
                .paid(paid);
        purchase = purchaseRepository.save(purchase);

        PointDto pointDto = new PointDto()
            .userId(userId)
            .count(paid * -1)
            .fromId(purchase.id())
            .fromType(PointService.PointType.PURCHASE.ordinal());
        pointService.save(pointDto);

        return Result.OK;
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
