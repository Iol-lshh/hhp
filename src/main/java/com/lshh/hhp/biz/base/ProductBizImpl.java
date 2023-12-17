package com.lshh.hhp.biz.base;

import com.lshh.hhp.dto.origin.ProductDto;
import com.lshh.hhp.dto.request.RequestPurchaseDto;
import com.lshh.hhp.orm.entity.Product;
import com.lshh.hhp.orm.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class ProductBizImpl implements ProductBiz {
    final ProductRepository productRepository;
    @Override
    public boolean validate(List<RequestPurchaseDto> purchaseRequestList) {
        return purchaseRequestList.stream()
                .filter(request -> productRepository.findById(request.getProductId()).isPresent())
                .count() != purchaseRequestList.size();
    }

    @Override
    public boolean isInStock(List<RequestPurchaseDto> purchaseRequestList) {
        return purchaseRequestList.size()
                == purchaseRequestList
                .stream()
                .filter(request-> productRepository.findFirstByIdAndStockCntGreaterThan(request.getProductId(), request.getCount()).isEmpty())
                .count();

    }

    @Override
    public int findPrice(Long productId) {
        return productRepository.findById(productId).map(Product::price).orElse(0);
    }

    @Override
    public List<ProductDto> unstore(List<RequestPurchaseDto> purchaseList) throws Exception {
        List<Product> products = purchaseList.stream()
                .map(request->productRepository.findById(request.getProductId())
                        .map(product->product.price(product.stockCnt() - request.getCount()))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        if(products.size()!=purchaseList.size()){
            throw new Exception("오류");
        }
        return productRepository
                .saveAll(products)
                .stream()
                .map(ProductBiz::toDto)
                .toList();
    }

    @Override
    public List<ProductDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductBiz::toDto)
                .toList();
    }

    @Override
    public Optional<ProductDto> find(Long productId) {
        return productRepository.findById(productId).map(ProductBiz::toDto);
    }
}
