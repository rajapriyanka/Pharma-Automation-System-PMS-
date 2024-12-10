package com.pms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.model.Drug;
import com.pms.model.Stock;
import com.pms.repository.DrugRepository;
import com.pms.repository.StockRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StockService {

    @Autowired
    private DrugRepository drugRepository;
    
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private EmailService emailService;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Transactional
    public Stock addStock(Stock stock) {
        if (stock.getDrug() == null || stock.getDrug().getId() == null) {
            throw new IllegalArgumentException("Drug ID must be provided");
        }

        Drug drug = drugRepository.findById(stock.getDrug().getId())
            .orElseThrow(() -> new EntityNotFoundException("Drug not found with id: " + stock.getDrug().getId()));

        stock.setDrug(drug);
        Stock savedStock = stockRepository.save(stock);

        drug.setTotalQuantity(drug.getTotalQuantity() + stock.getQuantity());
        drugRepository.save(drug);

        return savedStock;
    }

    @Transactional
    public Stock updateStock(Long id, Stock stockDetails) {
        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Stock not found with id: " + id));

        int quantityDifference = stockDetails.getQuantity() - stock.getQuantity();

        stock.setQuantity(stockDetails.getQuantity());
        stock.setExpiryDate(stockDetails.getExpiryDate());
        stock.setManufacturingDate(stockDetails.getManufacturingDate());
        stock.setThreshold(stockDetails.getThreshold());

        Drug drug = stock.getDrug();
        drug.setTotalQuantity(drug.getTotalQuantity() + quantityDifference);
        drugRepository.save(drug);

        return stockRepository.save(stock);
    }

    @Transactional
    public void deleteStock(Long id) {
        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Stock not found with id: " + id));
        
        Drug drug = stock.getDrug();
        drug.setTotalQuantity(drug.getTotalQuantity() - stock.getQuantity());
        drugRepository.save(drug);
        
        stockRepository.delete(stock);
    }

    public List<Stock> getStocksBelowThreshold() {
        return stockRepository.findStocksBelowThreshold();
    }

    public String sendReorderNotification() {
        List<Stock> stocks = stockRepository.findAll();

        for (Stock stock : stocks) {
            if (stock.getQuantity() < stock.getThreshold()) {
                Drug drug = stock.getDrug();
                String subject = "Reorder Notification for Drug: " + drug.getName();
                String body = "The stock for drug " + drug.getName()
                            + " (Batch No: " + stock.getBatchNo() + ") is below the threshold. Please reorder.";
                
                emailService.sendEmail("rajapriyanka11.01.2004@gmail.com", subject, body);
            }
        }
        return "Mail sent.";
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void scheduledReorderNotifications() {
        String result = sendReorderNotification();
        System.out.println(result);
    }
}

