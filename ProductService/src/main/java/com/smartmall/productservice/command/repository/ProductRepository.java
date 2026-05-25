package com.smartmall.productservice.command.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.common.dto.ProductResponse;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
	 Page<Product> findByNameContainingIgnoreCase(
	            String name,Pageable pageable);

	Optional<Product> findByProductCode(String productCode);
	
    @Query("""
            SELECT new com.smartmall.productservice.common.dto.ProductResponse(
                p.id,
                p.productCode,
                p.name,
                p.description,
                p.price,
                p.quantity,
                null,
                p.averageRating,
                c.name
            )
            FROM Product p
            LEFT JOIN p.category c
            WHERE p.productCode = :code
            """)
    Optional<ProductResponse> findResponseByProductCode(
            @Param("code") String code);
}