package com.smartmall.productservice.command.controller;

import com.smartmall.productservice.command.entity.Product;

import com.smartmall.productservice.command.service.ProductCommandService;
import com.smartmall.productservice.common.dto.ReviewRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commands/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final ProductCommandService service;

    @PostMapping
    public Product create(
            @RequestBody Product product) {

        return service.createProduct(product);
    }
    
    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody Product product) {

        return service.updateProduct(id, product);
    }
    
    @PostMapping("/{id}/reviews")
    public Product addReview(
            @PathVariable Long id,
            @RequestBody ReviewRequest request) {

        return service.addReview(id, request);
    }
}