package com.smartmall.productservice.command.controller;

import com.smartmall.productservice.command.entity.Product;

import com.smartmall.productservice.command.service.ProductCommandService;
import com.smartmall.productservice.common.dto.ProductResponse;
import com.smartmall.productservice.common.dto.ReviewRequest;
import com.smartmall.productservice.common.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commands/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final ProductCommandService service;

    @PostMapping
    public ProductResponse create(
            @RequestBody Product product) {
    	
    	Product saved = service.createProduct(product);

        return ProductMapper.mapToResponse(saved);
    }
    
    @PutMapping("/{productCode}")
    public ProductResponse update(
            @PathVariable String productCode,
            @RequestBody Product product) {

    	Product updated = service.updateProduct(productCode, product);
        return ProductMapper.mapToResponse(updated);
    }
    
    @PostMapping("/{productCode}/reviews")
    public ProductResponse addReview(
            @PathVariable String productCode,
            @RequestBody ReviewRequest request) {
    	
    	Product updated = service.addReview(productCode, request);

        return ProductMapper.mapToResponse(updated);
    }
    
    @GetMapping("/{productCode}")
    public ProductResponse getProduct(
            @PathVariable String productCode) {

        Product product =
                service.getProductByCode(
                        productCode);

        return ProductMapper.mapToResponse(
                product);
    }
}