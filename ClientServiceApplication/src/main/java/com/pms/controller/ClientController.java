package com.pms.controller;

import com.pms.model.Drug;
import com.pms.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Controller
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final RestTemplate restTemplate;
    private final String backendBaseUrl;

    @Autowired
    public ClientController(RestTemplate restTemplate, @Value("${backend.base-url}") String backendBaseUrl) {
        this.restTemplate = restTemplate;
        this.backendBaseUrl = backendBaseUrl;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/drugs";
    }

    @GetMapping("/drugs")
    public String getDrugManagement(Model model) {
        try {
            logger.info("Fetching drugs from {}", backendBaseUrl + "/api/drugs");
            ResponseEntity<List<Drug>> response = restTemplate.exchange(
                backendBaseUrl + "/api/drugs",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Drug>>() {}
            );
            model.addAttribute("drugs", response.getBody());
            return "drug-management";
        } catch (Exception e) {
            logger.error("Error fetching drugs", e);
            model.addAttribute("error", "Error fetching drugs: " + e.getMessage());
            model.addAttribute("drugs", Collections.emptyList());
            return "drug-management";
        }
    }

    @GetMapping("/drugs/add")
    public String addDrugForm(Model model) {
        model.addAttribute("drug", new Drug());
        return "add-drug";
    }

    @PostMapping("/drugs/add")
    public String addDrug(@ModelAttribute Drug drug, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Adding new drug: {}", drug);
            HttpEntity<Drug> request = new HttpEntity<>(drug);
            ResponseEntity<Drug> response = restTemplate.exchange(
                backendBaseUrl + "/api/drugs/add",
                HttpMethod.POST,
                request,
                Drug.class
            );
            logger.info("Drug added successfully: {}", response.getBody());
            redirectAttributes.addFlashAttribute("success", "Drug added successfully");
            return "redirect:/drugs";
        } catch (Exception e) {
            logger.error("Error adding drug", e);
            redirectAttributes.addFlashAttribute("error", "Error adding drug: " + e.getMessage());
            return "redirect:/drugs";
        }
    }

    @GetMapping("/drugs/update/{id}")
    public String updateDrugForm(@PathVariable Long id, Model model) {
        try {
            ResponseEntity<Drug> response = restTemplate.getForEntity(
                backendBaseUrl + "/api/drugs/" + id,
                Drug.class
            );
            model.addAttribute("drug", response.getBody());
            return "edit-drug";
        } catch (Exception e) {
            logger.error("Error fetching drug for editing", e);
            model.addAttribute("error", "Error fetching drug: " + e.getMessage());
            return "redirect:/drugs";
        }
    }

    @PostMapping("/drugs/update")
    public String updateDrug(@ModelAttribute Drug drug, RedirectAttributes redirectAttributes) {
        try {
            HttpEntity<Drug> request = new HttpEntity<>(drug);
            ResponseEntity<Drug> response = restTemplate.exchange(
                backendBaseUrl + "/api/drugs/update",
                HttpMethod.PUT,
                request,
                Drug.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute("success", "Drug updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Error updating drug: " + response.getStatusCode());
            }
            return "redirect:/drugs";
        } catch (Exception e) {
            logger.error("Error updating drug", e);
            redirectAttributes.addFlashAttribute("error", "Error updating drug: " + e.getMessage());
            return "redirect:/drugs";
        }
    }

    @PostMapping("/drugs/delete")
    public String deleteDrug(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            restTemplate.delete(backendBaseUrl + "/api/drugs/delete?id=" + id);
            redirectAttributes.addFlashAttribute("success", "Drug deleted successfully");
            return "redirect:/drugs";
        } catch (Exception e) {
            logger.error("Error deleting drug", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting drug: " + e.getMessage());
            return "redirect:/drugs";
        }
    }

    @PostMapping("/drugs/deactivate")
    public String deactivateDrug(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            restTemplate.postForObject(backendBaseUrl + "/api/drugs/deactivate?id=" + id, null, Void.class);
            redirectAttributes.addFlashAttribute("success", "Drug deactivated successfully");
            return "redirect:/drugs";
        } catch (Exception e) {
            logger.error("Error deactivating drug", e);
            redirectAttributes.addFlashAttribute("error", "Error deactivating drug: " + e.getMessage());
            return "redirect:/drugs";
        }
    }

    @GetMapping("/stocks")
    public String getStockManagement(Model model) {
        try {
            ResponseEntity<List<Stock>> stockResponse = restTemplate.exchange(
                backendBaseUrl + "/api/stocks",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Stock>>() {}
            );
            model.addAttribute("stocks", stockResponse.getBody());

            ResponseEntity<List<Drug>> drugResponse = restTemplate.exchange(
                backendBaseUrl + "/api/drugs",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Drug>>() {}
            );
            model.addAttribute("drugs", drugResponse.getBody());

            return "stock-management";
        } catch (Exception e) {
            logger.error("Error fetching stocks", e);
            model.addAttribute("error", "Error fetching stocks: " + e.getMessage());
            model.addAttribute("stocks", Collections.emptyList());
            model.addAttribute("drugs", Collections.emptyList());
            return "stock-management";
        }
    }

    @PostMapping("/stocks/add")
    public String addStock(@ModelAttribute Stock stock, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Adding new stock: {}", stock);
            HttpEntity<Stock> request = new HttpEntity<>(stock);
            ResponseEntity<Stock> response = restTemplate.exchange(
                backendBaseUrl + "/api/stocks/add",
                HttpMethod.POST,
                request,
                Stock.class
            );
            logger.info("Stock added successfully: {}", response.getBody());
            redirectAttributes.addFlashAttribute("success", "Stock added successfully");
            return "redirect:/stocks";
        } catch (Exception e) {
            logger.error("Error adding stock", e);
            redirectAttributes.addFlashAttribute("error", "Error adding stock: " + e.getMessage());
            return "redirect:/stocks";
        }
    }

    @PostMapping("/stocks/update")
    public String updateStock(@RequestParam Long id, @RequestParam Integer quantity, @RequestParam Integer threshold, RedirectAttributes redirectAttributes) {
        try {
            String url = backendBaseUrl + "/api/stocks/update/" + id;
            Stock stockUpdate = new Stock();
            stockUpdate.setQuantity(quantity);
            stockUpdate.setThreshold(threshold);

            HttpEntity<Stock> request = new HttpEntity<>(stockUpdate);
            ResponseEntity<Stock> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                Stock.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute("success", "Stock updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Error updating stock: " + response.getStatusCode());
            }
            return "redirect:/stocks";
        } catch (Exception e) {
            logger.error("Error updating stock", e);
            redirectAttributes.addFlashAttribute("error", "Error updating stock: " + e.getMessage());
            return "redirect:/stocks";
        }
    }

    @PostMapping("/stocks/delete")
    public String deleteStock(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            restTemplate.delete(backendBaseUrl + "/api/stocks/delete?id=" + id);
            redirectAttributes.addFlashAttribute("success", "Stock deleted successfully");
            return "redirect:/stocks";
        } catch (Exception e) {
            logger.error("Error deleting stock", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting stock: " + e.getMessage());
            return "redirect:/stocks";
        }
    }

    @GetMapping("/stocks/below-threshold")
    public String getStocksBelowThreshold(Model model) {
        try {
            ResponseEntity<List<Stock>> response = restTemplate.exchange(
                backendBaseUrl + "/api/stocks/below-threshold",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Stock>>() {}
            );
            model.addAttribute("stocks", response.getBody());
            return "stocks-below-threshold";
        } catch (Exception e) {
            logger.error("Error fetching stocks below threshold", e);
            model.addAttribute("error", "Error fetching stocks below threshold: " + e.getMessage());
            model.addAttribute("stocks", Collections.emptyList());
            return "stocks-below-threshold";
        }
    }
}

