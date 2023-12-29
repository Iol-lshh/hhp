package com.lshh.hhp.biz.base;

import com.lshh.hhp.biz.Biz;
import com.lshh.hhp.dto.origin.ProductDto;
import com.lshh.hhp.dto.origin.PurchaseDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.orm.entity.Product;
import com.lshh.hhp.orm.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Biz
public class ProductBizImpl implements ProductBiz {
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
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<ProductDto> unstore(List<RequestPurchaseDto> purchaseList) throws Exception {

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
                .map(ProductBiz::toDto)
                .toList();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void tryUnstore(List<RequestPurchaseDto> purchaseList) throws Exception {
        unstore(purchaseList);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<ProductDto> restore(List<PurchaseDto> purchasedList) throws Exception {

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
                .map(ProductBiz::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductBiz::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDto> find(Long productId) {
        return productRepository
                .findById(productId)
                .map(ProductBiz::toDto);
    }
}
