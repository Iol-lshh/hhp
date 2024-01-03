package com.lshh.hhp.product.service;

import com.lshh.hhp.common.Biz;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.product.Product;
import com.lshh.hhp.product.dto.ProductDto;
import com.lshh.hhp.product.repository.ProductRepository;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Biz
public class ProductBaseImpl implements ProductBase {
    final ProductRepository productRepository;
    @Override
    @Transactional(readOnly = true)
    public boolean validate(List<RequestPurchaseDto> purchaseRequestList) {
        return purchaseRequestList.size() == purchaseRequestList.stream()
                .filter(request -> productRepository.findById(request.getProductId())
                        .isPresent())
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInStock(List<RequestPurchaseDto> purchaseRequestList) {
        return purchaseRequestList
                .stream()
                .allMatch(request -> productRepository.findFirstByIdAndStockCntGreaterThanEqual(request.getProductId(), request.getCount())
                        .isPresent());
    }

    @Override
    @Transactional(readOnly = true)
    public int findPrice(Long productId) {
        return productRepository.findById(productId)
                .map(Product::price)
                .orElse(0);
    }

    @Override
    @Lock(LockModeType.OPTIMISTIC)
    @Transactional
    public List<ProductDto> deduct(List<RequestPurchaseDto> purchaseList) throws Exception {

        // ## 재고 확인
        if (!isInStock(purchaseList)) {
            throw new Exception("재고 부족");
        }

        // ## 처리
        List<Product> products = purchaseList.stream()
                .map(request->productRepository.findById(request.getProductId())
                        .map(product->product.stockCnt(product.stockCnt() - request.getCount()))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        if(products.size()!=purchaseList.size()){
            throw new Exception("상품 정보 오류");
        }
        return productRepository
                .saveAll(products)
                .stream()
                .map(ProductBase::toDto)
                .toList();
    }

    @Override
    @Lock(LockModeType.OPTIMISTIC)
    public List<ProductDto> conduct(List<OrderItemDto> purchasedList) throws Exception {

        List<Product> products = purchasedList.stream()
                .map(purchased -> productRepository.findById(purchased.productId())
                        .map(product -> product.stockCnt(product.stockCnt() + purchased.count())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        if(products.size()!=purchasedList.size()){
            throw new Exception("상품 정보 오류");
        }
        return productRepository
                .saveAll(products)
                .stream()
                .map(ProductBase::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductBase::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDto> find(Long productId) {
        return productRepository
                .findById(productId)
                .map(ProductBase::toDto);
    }

}
