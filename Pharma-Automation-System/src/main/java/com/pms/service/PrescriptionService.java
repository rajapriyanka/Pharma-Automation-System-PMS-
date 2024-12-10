package com.pms.service;

import com.pms.model.Drug;
import com.pms.model.Prescription;
import com.pms.model.Stock;
import com.pms.repository.DrugRepository;
import com.pms.repository.PrescriptionRepository;
import com.pms.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private StockRepository stockRepository;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prescription not found with id: " + id));
    }

    @Transactional
    public Prescription addPrescription(Prescription prescription) {
        prescription.getItems().forEach(item -> {
            Drug drug = drugRepository.findById(item.getDrug().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Drug not found with id: " + item.getDrug().getId()));
            
            if (drug.getTotalQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for drug: " + drug.getName());
            }
            
            drug.setTotalQuantity(drug.getTotalQuantity() - item.getQuantity());
            drugRepository.save(drug);
            
            List<Stock> stocks = stockRepository.findByDrugIdOrderByExpiryDateAsc(drug.getId());
            int remainingQuantity = item.getQuantity();
            
            for (Stock stock : stocks) {
                if (remainingQuantity <= 0) break;
                
                if (stock.getQuantity() <= remainingQuantity) {
                    remainingQuantity -= stock.getQuantity();
                    stock.setQuantity(0);
                } else {
                    stock.setQuantity(stock.getQuantity() - remainingQuantity);
                    remainingQuantity = 0;
                }
                
                stockRepository.save(stock);
            }
        });
        
        return prescriptionRepository.save(prescription);
    }
}

