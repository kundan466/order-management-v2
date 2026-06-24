package com.order_management.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order_management.demo.dto.InventoryResponseDTO;
import com.order_management.demo.Utils.CurrentUserDetails;
import com.order_management.demo.dto.InventoryRequestDTO;
import com.order_management.demo.entity.Inventory;
import com.order_management.demo.entity.Product;
import com.order_management.demo.mapper.InventoryMapper;
import com.order_management.demo.repository.InventoryRepository;
import com.order_management.demo.repository.ProductRepository;
import com.order_management.demo.exception.InventoryNotFoundException;
import com.order_management.demo.exception.ProductNotFoundException;


@Service
public class InventoryService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    CurrentUserDetails currentUserDetails;



    public String addInventoryItems(InventoryRequestDTO entity) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'addInventoryItems'");
        Product product = productRepository.findById(entity.getProductId()).orElseThrow(() -> {
            return new ProductNotFoundException("Product not found");
        });
        // Create a new Inventory entity and set its properties based on the request DTO
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setAvailableStock(entity.getAvailableStock());
        inventory.setReservedStock(entity.getReservedStock());
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());
        inventory.setCreatedBy(currentUserDetails.getUserDetails().getId());
        inventory.setUpdatedBy(currentUserDetails.getUserDetails().getId());
        inventoryRepository.save(inventory);
        return "Inventory items added successfully - " + inventory.getId();
    }



    public List<InventoryResponseDTO> getInventoryItems() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getInventoryItems'");
        List<Inventory> inventoryItems = inventoryRepository.findAll();
        return inventoryItems.stream().map(InventoryMapper::toResponseDTO).toList();
    }



    public void updateInventoryItems(Long id, InventoryRequestDTO entity) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateInventoryItems'");
        Inventory existingInventory = inventoryRepository.findById(id).orElseThrow(() -> {
            return new InventoryNotFoundException("Inventory not found");
        });

        //  try {
        //     Thread.sleep(10000);
        // }catch(Exception e) {}

        existingInventory.setProduct(productRepository.findById(entity.getProductId()).orElseThrow(() -> {
            return new ProductNotFoundException("Product not found");
        }));
        existingInventory.setId(id);
        existingInventory.setAvailableStock(entity.getAvailableStock());
        existingInventory.setReservedStock(entity.getReservedStock());
   
        existingInventory.setUpdatedAt(LocalDateTime.now());
        existingInventory.setUpdatedBy(currentUserDetails.getUserDetails().getId());

        inventoryRepository.save(existingInventory);
    }



    public String deleteInventoryItems(Long id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'deleteInventoryItems'");
        Inventory existingInventory = inventoryRepository.findById(id).orElseThrow(() -> {
            return new InventoryNotFoundException("Inventory not found");
        });
        Product product = existingInventory.getProduct();
        if (product != null) {
            product.setInventory(null);
        }
        inventoryRepository.delete(existingInventory);
        return "Inventory deleted successfully - " + id;
    }
    
}
