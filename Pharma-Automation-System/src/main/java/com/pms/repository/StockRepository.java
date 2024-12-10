package com.pms.repository;

import com.pms.model.Stock;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT SUM(s.quantity) FROM Stock s WHERE s.drug.id = :drugId")
    int getTotalQuantityByDrugId(@Param("drugId") Long drugId);

    @Query("SELECT s FROM Stock s WHERE s.quantity < s.threshold")
    List<Stock> findStocksBelowThreshold();

    Optional<Stock> findByBatchNo(Long batchNo);

	List<Stock> findByDrugIdOrderByExpiryDateAsc(Long id);
}

