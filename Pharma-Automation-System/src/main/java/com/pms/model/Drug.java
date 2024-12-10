package com.pms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "drugs")
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Drug name cannot be empty")
    private String name;
  

    private String description;
    
    @Column(nullable = false)
    private Double price;
  
    
    private String supplierName;
    
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity = 0;
    
    private boolean active = true;
    
    private boolean banned = false;
    
    private String bannedReason;
    
   

    

//    // Getters and setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public Double getPrice() {
//        return price;
//    }
//
//    public void setPrice(Double price) {
//        this.price = price;
//    }
//
//    public String getSupplierName() {
//        return supplierName;
//    }
//
//    public void setSupplierName(String supplierName) {
//        this.supplierName = supplierName;
//    }
//
//    public Integer getTotalQuantity() {
//        return totalQuantity;
//    }
//
//    public void setTotalQuantity(Integer totalQuantity) {
//        this.totalQuantity = totalQuantity;
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean active) {
//        this.active = active;
//    }
//
//    public boolean isBanned() {
//        return banned;
//    }
//
//    public void setBanned(boolean banned) {
//        this.banned = banned;
//    }
//
//    public String getBannedReason() {
//        return bannedReason;
//    }
//
//    public void setBannedReason(String bannedReason) {
//        this.bannedReason = bannedReason;
//    }
//
//   
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
}