package com.order_management.demo.service;

import com.order_management.demo.Utils.CurrentUserDetails;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.order_management.demo.Utils.Constants;
import com.order_management.demo.config.RedisConfig;
import com.order_management.demo.dto.ProductRequestDTO;
import com.order_management.demo.dto.ProductResponseDTO;
import com.order_management.demo.entity.Product;
import com.order_management.demo.mapper.ProductMapper;
import com.order_management.demo.repository.ProductRepository;



import com.order_management.demo.exception.ProductNotFoundException;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private CurrentUserDetails currentUserDetails;

    private final ProductRepository productRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectmapper;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        
    }

    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDto) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'addProduct'");
        Product product= ProductMapper.toEntity(productRequestDto);
        // Save the product entity to the database (using a repository)
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setCreatedBy(currentUserDetails.getUserDetails().getId());
        product.setUpdatedBy(currentUserDetails.getUserDetails().getId());
        productRepository.save(product);
        log.info("Product added with id: {}", product.getId());
        //cache invalidation
        redisTemplate.delete(Constants.PRODUCTS_CACHE_KEY);
        log.info("Products cache invalidated");

        return ProductMapper.toDTO(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getAllProducts'");
        
        //before hitting the db, checcking it in redis cache
      
        String cachedJson =
        (String) redisTemplate
                .opsForValue()
                .get(Constants.PRODUCTS_CACHE_KEY);

        // List<ProductResponseDTO> cachedProducts= (List<ProductResponseDTO>) redisTemplate.opsForValue().get(Constants.PRODUCTS_CACHE_KEY);
        if(cachedJson!=null){
            log.info("Products are getting from cache");
            return objectmapper.readValue(cachedJson, new TypeReference<
                    List<ProductResponseDTO>>() {});
        }
        
        log.info("Products are returning from DB!");
        
        List<Product> products = productRepository.findAll();
        log.info("Retrieved all products: {}", products.size());

        String json = objectmapper.writeValueAsString(products.stream().map(ProductMapper::toDTO).toList());

        // redisTemplate.opsForValue().set(Constants.PRODUCTS_CACHE_KEY,products.stream().map(ProductMapper::toDTO).toList());
        redisTemplate.opsForValue().set(Constants.PRODUCTS_CACHE_KEY,json,10,TimeUnit.MINUTES);

        return products.stream().map(ProductMapper::toDTO).toList();
    }

    public String updateProduct(Long id, ProductRequestDTO entity) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> {
            log.warn("Attempted to update non-existent product with id: {}", id);
            return new ProductNotFoundException("Product not found");
        });
        // Update the existing product with the new entity data
        existingProduct.setName(entity.getName());
        existingProduct.setCategory(entity.getCategory());
        existingProduct.setDescription(entity.getDescription());
        existingProduct.setBrand(entity.getBrand());
        existingProduct.setDealerName(entity.getDealerName());
        existingProduct.setOriginalPrice(entity.getOriginalPrice());
        existingProduct.setSellingPrice(entity.getSellingPrice());
        existingProduct.setDiscount(entity.getDiscount());
        existingProduct.setId(id); // Ensure the ID is set to the existing product's ID
        // Save the updated product entity to the database

        
        existingProduct.setUpdatedAt(LocalDateTime.now());
        existingProduct.setUpdatedBy(currentUserDetails.getUserDetails().getId());
        productRepository.save(existingProduct);
        log.info("Product updated with id: {}", id);

        redisTemplate.delete(Constants.PRODUCTS_CACHE_KEY);
        log.info("Products cache invalidated");

        return "Product updated successfully - " +id;
    }

    public String deleteProduct(Long id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'deleteProduct'");
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            log.info("Deleting product with id: {}", id);

            redisTemplate.delete(Constants.PRODUCTS_CACHE_KEY);
            log.info("Products cache invalidated");
            
            return "Product deleted successfully - " + id;
        } else {
            log.warn("Attempted to delete non-existent product with id: {}", id);
            return "Product not found";
        }
        
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponseDTO getProductById(Long id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getProductById'");
        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.warn("Attempted to retrieve non-existent product with id: {}", id);
            return new ProductNotFoundException("Product not found");
        });
        log.info("Retrieved product with id: {}", id);
        return ProductMapper.toDTO(product);
    }

    public Page<ProductResponseDTO> getAllProducts(int page, int size, String sortBy) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getAllProducts'");
        Page<Product> productsPage = productRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
        log.info("Retrieved products page: {} with size: {}", page, size);
        return productsPage.map(ProductMapper::toDTO);
    }

    public List<ProductResponseDTO> getProductsByFilter(String brand, String category) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getProductsByFilter'");
        List<Product> products = productRepository.findByBrandAndCategory(brand, category);
        return products.stream().map(ProductMapper::toDTO).toList();
    }


    
}
