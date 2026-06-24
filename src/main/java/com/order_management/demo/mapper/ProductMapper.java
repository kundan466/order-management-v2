package com.order_management.demo.mapper;


import org.springframework.stereotype.Component;

import com.order_management.demo.dto.ProductRequestDTO;
import com.order_management.demo.dto.ProductResponseDTO;
import com.order_management.demo.entity.Product;

@Component
public class ProductMapper {
     public static ProductResponseDTO toDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        dto.setBrand(product.getBrand());
        dto.setDealerName(product.getDealerName());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setSellingPrice(product.getSellingPrice());
        dto.setDiscount(product.getDiscount());
        return dto;
    }

    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setBrand(dto.getBrand());
        product.setDealerName(dto.getDealerName());
        product.setOriginalPrice(dto.getOriginalPrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setDiscount(dto.getDiscount());
        return product;
    }
}
