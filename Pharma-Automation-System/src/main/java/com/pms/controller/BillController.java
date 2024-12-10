package com.pms.controller;

import com.pms.model.Bill;
import com.pms.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }

    @PostMapping("/generate/{prescriptionId}")
    public ResponseEntity<Bill> generateBill(@PathVariable Long prescriptionId) {
        Bill generatedBill = billService.generateBill(prescriptionId);
        return ResponseEntity.ok(generatedBill);
    }
}

