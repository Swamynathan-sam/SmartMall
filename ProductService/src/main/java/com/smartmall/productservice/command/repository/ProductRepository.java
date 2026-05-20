package com.smartmall.productservice.command.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartmall.productservice.command.entity.Product;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
	 List<Product> findByNameContainingIgnoreCase(
	            String name);

	Optional<Product> findByProductCode(String productCode);
}