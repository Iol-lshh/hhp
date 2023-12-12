package com.lshh.hhp.service;

import com.lshh.hhp.common.dto.Response.Result;
import com.lshh.hhp.common.dto.ResultDto;
import com.lshh.hhp.dto.StockDto;
import com.lshh.hhp.orm.entity.Stock;
import com.lshh.hhp.orm.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@AllArgsConstructor
@Service
public class StockServiceDefault implements StockService{

    final StockRepository stockRepository;

    public static StockDto toDto(Stock entity){
        return new StockDto()
                .id(entity.id())
                .productId(entity.productId())
                .purchaseId(entity.purchaseId());
    }
    public static Stock toEntity(StockDto dto){
        return new Stock()
                .id(dto.id())
                .productId(dto.productId())
                .purchaseId(dto.purchaseId());
    }

    @Override
    @Transactional
    public ResultDto<StockDto> save(StockDto dto) throws Exception {
        Stock stock;
        if(Optional.ofNullable(dto.id()).isEmpty()){
            stock = StockServiceDefault.toEntity(dto);
        }else{
            stock = stockRepository
                    .findById(dto.id())
                    .orElseThrow(Exception::new);
            stock.purchaseId(dto.purchaseId() == null ? stock.purchaseId() : dto.purchaseId());
        }

        dto = StockServiceDefault.toDto(stockRepository.save(stock));

        return new ResultDto<>(Result.OK, dto);
    }

    @Override
    public List<StockDto> findAll() {
        return stockRepository
                .findAll()
                .stream()
                .map(StockServiceDefault::toDto)
                .toList();
    }

    @Override
    public Optional<StockDto> find(long id) {
        return stockRepository
                .findById(id)
                .map(StockServiceDefault::toDto);
    }

    @Override
    public List<StockDto> findAllByProductId(long productId) {
        return stockRepository
                .findAllByProductId(productId)
                .stream()
                .map(StockServiceDefault::toDto)
                .toList();
    }

    @Override
    public List<StockDto> findAllInStockByProductId(long productId) {
        return stockRepository
                .findAllByProductIdAndPurchaseIdIsNull(productId)
                .stream()
                .map(StockServiceDefault::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ResultDto<List<StockDto>> input(long productId, int cnt) {
        List<Stock> stocks = IntStream
                .rangeClosed(1, cnt)
                .mapToObj(e->new Stock().productId(productId))
                .toList();
        List<StockDto> dtos = stockRepository
                .saveAllAndFlush(stocks)
                .stream()
                .map(StockServiceDefault::toDto)
                .toList();
        return new ResultDto<>(Result.OK, dtos);
    }

    @Override
    @Transactional
    public ResultDto<StockDto> output(long productId, long purchaseId) throws Exception {
        StockDto dto = findAllInStockByProductId(productId)
                .stream()
                .findFirst()
                .orElseThrow(Exception::new);

        dto.purchaseId(purchaseId);
        return this.save(dto);
    }

    @Override
    public boolean isAllInStock(List<Long> productIdList) {
        // todo
        return false;
    }
}
