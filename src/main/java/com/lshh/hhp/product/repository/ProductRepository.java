package com.lshh.hhp.product.repository;

import com.lshh.hhp.product.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findFirstByIdAndStockCntGreaterThanEqual(Long id, Integer stockCnt);

    @Query(
           value = "with sum_paid_cnt AS (" +
                    " select" +
                     " toi.product_id," +
                     " sum(toi.count) as paid_cnt" +
                    " from tb_order_item toi" +
                    " group by toi.product_id" +
                   ")" +
                   " select tp.*" +
//                    ", spc.paid_cnt" +
//                    ", row_number() over(order by spc.paid_cnt desc) as order_by_paid_cnt" +
                   " from sum_paid_cnt spc" +
                   " inner join tb_product tp" +
                    " on tp.id = spc.product_id" +
                   " order by paid_cnt desc" +
                   " limit :pageSize" +
                   " offset :pageNo"
            , nativeQuery = true
    )
    List<Product> findTopPurchased(int pageSize, int pageNo);
}
