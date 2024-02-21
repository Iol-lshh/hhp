package com.lshh.hhp.domain.product;

import com.lshh.hhp.common.annotation.Service;
import com.lshh.hhp.common.exception.BusinessException;
import com.lshh.hhp.domain.order.item.OrderItem;
import com.lshh.hhp.domain.product.dto.RequestProductDto;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductService{
    final ProductRepository productRepository;
    @Transactional(readOnly = true)
    public boolean validate(List<RequestProductDto> purchaseRequestList) {
        return purchaseRequestList.size() == purchaseRequestList.stream()
                .filter(request -> productRepository.findById(request.getProductId())
                        .isPresent())
                .count();
    }

    @Transactional(readOnly = true)
    public boolean isInStock(List<OrderItem> orderItems) {
        return orderItems
                .stream()
                .allMatch(request -> productRepository.findFirstByIdAndStockCntGreaterThanEqual(request.productId(), request.count())
                        .isPresent());
    }

    @Transactional(readOnly = true)
    public int findPrice(Long productId) {
        return productRepository.findById(productId)
                .map(Product::price)
                .orElse(0);
    }

    @Transactional
    public List<Product> deduct(List<OrderItem> orderItems) {

        // ## 재고 확인
        if (!isInStock(orderItems)) {
            throw new BusinessException(String.format("""
                    ProductServiceImpl::deduct - 재고 없음 {%s}"""
                    , String.join(",", orderItems.stream().map(e->e.id().toString()).toList()) ));
        }

        // ## 처리
        List<Product> products = orderItems.stream()
                .map(request->productRepository.findById(request.productId())
                        .map(product->product.deduct(request.count()))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if(products.size()!=orderItems.size()){
            throw new BusinessException(String.format("""
                    ProductServiceImpl::deduct - 상품 정보 오류 {%s}"""
                    , String.join(",", orderItems.stream().map(e->e.id().toString()).toList()) ));
        }

        return productRepository.saveAllAndFlush(products);
    }

    @Transactional
    public List<Product> conduct(List<OrderItem> orderItems) {

        List<Product> products = orderItems.stream()
                .map(oi -> productRepository.findById(oi.productId())
                        .map(product -> product.conduct(oi.count()))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if(products.size()!=orderItems.size()){
            throw new BusinessException(String.format("""
                    ProductServiceImpl::conduct - 상품 정보 오류 {%s}"""
                    , String.join(",", orderItems.stream().map(e->e.id().toString()).toList()) ));
        }

        return productRepository.saveAllAndFlush(products);
    }

    @Transactional
    public List<Product> conductRequest(List<RequestProductDto> purchaseDtoList) {
        List<Product> products = purchaseDtoList.stream()
                .map(requestDto -> productRepository.findById(requestDto.getProductId())
                        .map(product -> product.conduct(requestDto.getCount()))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if(products.size()!=purchaseDtoList.size()){
            throw new BusinessException(String.format("""
                    ProductServiceImpl::conduct - 상품 정보 오류 {%s}"""
                    , String.join(",", purchaseDtoList.stream().map(e->e.getProductId().toString()).toList()) ));
        }

        return productRepository.saveAllAndFlush(products);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Product> find(Long productId) {
        return productRepository.findById(productId);
    }

    @Transactional
    public List<OrderItem> putPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.setPriceTag(
                        findPrice(orderItem.productId())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public int findPrice(long productId){
        return productRepository.findById(productId).map(Product::price).orElse(0);
    }

    @Transactional(readOnly = true)
    public List<Product> favorite(Integer count) {
        return productRepository.findTopPurchased(count, 0);
    }
}
