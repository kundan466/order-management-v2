package com.order_management.demo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.order_management.demo.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

   
    List<Product> findByBrandAndCategory(String brand, String category);


    List<Product> findByBrandContaining(String keyword);

    @Query("""
    SELECT p
    FROM Product p
    WHERE p.sellingPrice < :price
    """)
    List<Product> getProductsUnderPrice(@Param("price") Double maxPrice);

}
