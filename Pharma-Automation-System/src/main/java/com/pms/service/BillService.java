package com.pms.service;

import com.pms.model.Bill;
import com.pms.model.Prescription;
import com.pms.repository.BillRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private PrescriptionService prescriptionService;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found with id: " + id));
    }

    @Transactional
    public Bill generateBill(Long prescriptionId) {
        Prescription prescription = prescriptionService.getPrescriptionById(prescriptionId);
        
        double totalAmount = prescription.getItems().stream()
                .mapToDouble(item -> item.getDrug().getPrice() * item.getQuantity())
                .sum();
        
        double discountPercentage = 10.0;
        double discountedAmount = totalAmount * (1 - discountPercentage / 100);
        
        Bill bill = new Bill();
        bill.setPrescription(prescription);
        bill.setAmount(totalAmount);
        bill.setBillDate(LocalDate.now());
        bill.setDiscountPercentage(discountPercentage);
        bill.setDiscountedAmount(discountedAmount);
        
        return billRepository.save(bill);
    }
}

