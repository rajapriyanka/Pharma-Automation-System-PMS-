package com.pms.service;

import com.pms.model.Drug;
import com.pms.repository.DrugRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugService {

    @Autowired
    private DrugRepository drugRepository;

    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    public Drug getDrugById(Long id) {
        return drugRepository.findById(id).orElse(null);
    }
    @Transactional
    public Drug saveDrug(Drug drug) {
        validateDrug(drug);
        return drugRepository.save(drug);
    }

    private void validateDrug(Drug drug) {
    	if (drug == null || drug.getName() == null || drug.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Drug name cannot be empty");
        }
        if (drug.getPrice() == null || drug.getPrice() <= 0) {
            throw new IllegalArgumentException("Drug price must be greater than zero");
        }
        if (drug.getTotalQuantity() == null || drug.getTotalQuantity() <=0) {
            throw new IllegalArgumentException("Total quantity cannot be negative");
        }
       
    }

    @Transactional
    public Drug updateDrug(Drug drug) {
        if (drug.getId() == null) {
            throw new IllegalArgumentException("Drug ID cannot be null for update operation");
        }
        
        Drug existingDrug = drugRepository.findById(drug.getId())
            .orElseThrow();
        
        // Update the fields
        existingDrug.setName(drug.getName());
        existingDrug.setDescription(drug.getDescription());
        existingDrug.setPrice(drug.getPrice());
        existingDrug.setSupplierName(drug.getSupplierName());
        existingDrug.setTotalQuantity(drug.getTotalQuantity());
        existingDrug.setActive(drug.isActive());
        existingDrug.setBanned(drug.isBanned());
        existingDrug.setBannedReason(drug.getBannedReason());
       
        return drugRepository.save(existingDrug);
    }

    @Transactional
    public Drug deactivateDrug(Long id) {
        Drug drug = drugRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Drug not found with id: " + id));
        
        drug.setActive(false);
        return drugRepository.save(drug);
    }
    @Transactional
    public void deleteDrug(Long id) {
        Drug drug = drugRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Drug not found with id: " + id));
        
        drugRepository.delete(drug);
    }

}