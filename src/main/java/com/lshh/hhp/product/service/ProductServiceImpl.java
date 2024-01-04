package com.lshh.hhp.product.service;

import com.lshh.hhp.common.Service;
import com.lshh.hhp.orderItem.OrderItem;
import com.lshh.hhp.orderItem.dto.OrderItemDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.product.Product;
import com.lshh.hhp.product.repository.ProductRepository;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
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
    @Transactional
    public List<Product> deduct(List<RequestPurchaseDto> purchaseList) throws Exception {

        // ## 재고 확인
        if (!isInStock(purchaseList)) {
            throw new Exception("재고 부족");
        }

        // ## 처리
        List<Product> products = purchaseList.stream()
                .map(request->productRepository.findById(request.getProductId())
                        .map(product->product.deduct(request.getCount()))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        if(products.size()!=purchaseList.size()){
            throw new Exception("상품 정보 오류");
        }

        return productRepository.saveAll(products);
    }

    @Override
    @Transactional
    public List<Product> conduct(List<OrderItemDto> orderItemDtos) throws Exception {

        List<Product> products = orderItemDtos.stream()
                .map(orderItemDto -> productRepository.findById(orderItemDto.productId())
                        .map(product -> product.conduct(orderItemDto.count()))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if(products.size()!=orderItemDtos.size()){
            throw new Exception("상품 정보 오류");
        }

        return productRepository.saveAll(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> find(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    @Transactional
    public List<OrderItem> putPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
            .map(orderItem -> orderItem.setPriceTag(
                    findPrice(orderItem.productId())
            ))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public int findPrice(long productId){
        return productRepository.findById(productId).map(Product::price).orElse(0);
    }
}
