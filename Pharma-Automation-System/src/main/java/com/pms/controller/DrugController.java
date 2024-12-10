package com.pms.controller;

import com.pms.model.Drug;
import com.pms.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drugs")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @GetMapping
    public ResponseEntity<List<Drug>> getAllDrugs() {
        List<Drug> drugs = drugService.getAllDrugs();
        return ResponseEntity.ok(drugs);
    }

    @PostMapping("/add")
    public ResponseEntity<Drug> addDrug(@RequestBody Drug drug) {
        Drug savedDrug = drugService.saveDrug(drug);
        return ResponseEntity.ok(savedDrug);
    }

    @PutMapping("/update")
    public ResponseEntity<Drug> updateDrug(@RequestBody Drug drug) {
        Drug updatedDrug = drugService.updateDrug(drug);
        return ResponseEntity.ok(updatedDrug);
    }

    @PostMapping("/deactivate")
    public ResponseEntity<Void> deactivateDrug(@RequestParam Long id) {
        drugService.deactivateDrug(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteDrug(@RequestParam Long id) {
        drugService.deleteDrug(id);
        return ResponseEntity.ok().build();
    }
}

