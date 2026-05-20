package com.smartmall.productservice.query.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.common.dto.ProductResponse;
import com.smartmall.productservice.common.mapper.ProductMapper;
import com.smartmall.productservice.query.service.ProductQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/queries/products")
@RequiredArgsConstructor
public class ProductQueryController {

    private final ProductQueryService service;

    @GetMapping("/{productCode}")
    public ProductResponse getProduct(
            @PathVariable String productCode) {
        Product product =
                service.getProductByCode(
                        productCode);
        return ProductMapper.mapToResponse(
                product);
    }

    @GetMapping("/search")
    public List<Product> getByName(
            @RequestParam String name) {

        return service.getProductByName(name);
    }
}