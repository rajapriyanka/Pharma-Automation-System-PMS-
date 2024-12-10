package com.pms.controller;

import com.pms.model.Drug;
import com.pms.model.Stock;
import com.pms.service.StockService;
import com.pms.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private DrugService drugService;
   
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @PostMapping("/add")
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        Stock savedStock = stockService.addStock(stock);
        return ResponseEntity.ok(savedStock);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @RequestBody Stock stockDetails) {
        Stock updatedStock = stockService.updateStock(id, stockDetails);
        return ResponseEntity.ok(updatedStock);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteStock(@RequestParam Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/below-threshold")
    public ResponseEntity<List<Stock>> getStocksBelowThreshold() {
        List<Stock> stocks = stockService.getStocksBelowThreshold();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/drugs")
    public ResponseEntity<List<Drug>> getAllDrugs() {
        List<Drug> drugs = drugService.getAllDrugs();
        return ResponseEntity.ok(drugs);
    }
}

