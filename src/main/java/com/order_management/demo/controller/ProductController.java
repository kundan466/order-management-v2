package com.order_management.demo.controller;
import com.order_management.demo.repository.ProductRepository;
import org.springframework.web.bind.annotation.RestController;
import com.order_management.demo.dto.ProductRequestDTO;
import com.order_management.demo.dto.ProductResponseDTO;
import com.order_management.demo.mapper.ProductMapper;
import com.order_management.demo.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@RequestMapping("/api/products")
public class ProductController {

private final ProductRepository productRepository;
public ProductService productService;

public ProductController(ProductService productService, ProductRepository productRepository) {
    this.productService = productService;
    this.productRepository = productRepository;
}

@PostMapping("/addProduct")
public String getMethodName(@Valid @RequestBody ProductRequestDTO productRequestDto) {
    ProductResponseDTO response = productService.addProduct(productRequestDto); 
    return "Product added successfully with ID: " + response.getId(); 
}

@GetMapping("/getProducts") //working as expected
public List<ProductResponseDTO> getAllProducts() {
    return productService.getAllProducts();
}

@GetMapping("/getProducts/{id}") //working as expected
public ProductResponseDTO getProductById(@PathVariable Long id) {
    return productService.getProductById(id);
}


@PutMapping("/updateproduct/{id}")
public String putMethodName(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO entity) {
    //TODO: process PUT request
    String result= productService.updateProduct(id, entity);
    return result;
    
}
@DeleteMapping("/deleteproduct/{id}")
public String deleteMethodName(@PathVariable Long id) {
    //TODO: process DELETE request
    String result= productService.deleteProduct(id);
    return result;
}

@GetMapping("/getproducts")
public Page<ProductResponseDTO> getProducts(
        @RequestParam int page,
        @RequestParam int size, @RequestParam(defaultValue = "name") String sortBy){

    return productService
            .getAllProducts(page, size, sortBy);
}

@GetMapping("/getproducts/filter")
public List<ProductResponseDTO> getProductsByFilter(@RequestParam String brand,@RequestParam String category){
    return productService.getProductsByFilter(brand, category);
}

@GetMapping("/getproducts/search")
public List<ProductResponseDTO> getProductsBySearch(@RequestParam String keyword) {
    return productRepository.findByBrandContaining(keyword).stream().map(ProductMapper::toDTO).toList();
}

@GetMapping("/getproducts/under-price")
public List<ProductResponseDTO> getMethodName(@RequestParam Double param) {
    return productRepository.getProductsUnderPrice(param).stream().map(ProductMapper::toDTO).toList();
}



    
}
