package com.smartmall.productservice.query.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartmall.productservice.common.dto.ProductResponse;
import com.smartmall.productservice.query.service.ProductQueryService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/queries/products")
@RequiredArgsConstructor
public class ProductQueryController {

    private final ProductQueryService service;

    @Operation(
            summary = "Get product by code")
    @GetMapping("/{productCode}")
    public ProductResponse getProduct(
            @PathVariable String productCode) {
              return  service.getProductByCode(
                        productCode);
    }
    @Operation(
            summary = "Search products")
    @GetMapping("/search")
    public Page<ProductResponse> getByName(
            @RequestParam String name,Pageable pageable) {

        return service.getProductByName(name, pageable);
    }
}