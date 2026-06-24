package com.order_management.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order_management.demo.dto.InventoryRequestDTO;
import com.order_management.demo.dto.InventoryResponseDTO;
import com.order_management.demo.service.InventoryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import com.order_management.demo.exception.InventoryNotFoundException;




@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


   @PostMapping("/addinventory")
   public String addinventoryItems(@Valid @RequestBody InventoryRequestDTO entity) {
       //TODO: process POST request
       String result= inventoryService.addInventoryItems(entity);
       
       return result;
   }
   
    @GetMapping("/getinventory")
    public List<InventoryResponseDTO> getInventoryItems() {
        return inventoryService.getInventoryItems();
    }
    
    @GetMapping("/getinventory/{id}")
    public InventoryResponseDTO getInventoryByID(@PathVariable Long id) {
        // TODO: Implement logic to fetch inventory by ID
        return inventoryService.getInventoryItems().stream()
                .filter(inventory -> inventory.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with ID: " + id));
    }

    @PutMapping("/updateinventory/{id}")
    public String putMethodName(@PathVariable Long id, @Valid @RequestBody InventoryRequestDTO entity) {
        //TODO: process PUT request
        inventoryService.updateInventoryItems(id, entity);
        
        return "Inventory updated successfully - " + id;
    }
    
    @DeleteMapping("/deleteinventory/{id}")
    public String deleteMethodName(@PathVariable Long id) {
        
        String result= inventoryService.deleteInventoryItems(id);
        return result;
    }


    
}
