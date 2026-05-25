package com.smartmall.productservice.query.service;

import java.time.Duration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.repository.ProductRepository;
import com.smartmall.productservice.common.dto.ProductResponse;
import com.smartmall.productservice.common.mapper.ProductMapper;
import com.smartmall.productservice.exception.ProductNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final RedisTemplate<String, ProductResponse> redisTemplate;

    private final ProductRepository repository;

    // ---------------- GET BY ID ----------------

    public ProductResponse getProduct(Long id) {

        String key = "product:" + id;

        ProductResponse cached =
                redisTemplate
                        .opsForValue()
                        .get(key);

        if (cached != null) {
            return cached;
        }

        Product product =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product not found"));

        ProductResponse response =
                ProductMapper.mapToResponse(
                        product);

        redisTemplate
                .opsForValue()
                .set(
                        key,
                        response,
                        Duration.ofMinutes(10));

        return response;
    }

    // ---------------- GET BY CODE ----------------

    public ProductResponse getProductByCode(
            String productCode) {

        String key =
                "productCode:" +
                productCode;

        ProductResponse cached =
                redisTemplate
                        .opsForValue()
                        .get(key);

        if (cached != null) {
            return cached;
        }

        ProductResponse response =
                repository
                        .findResponseByProductCode(
                                productCode)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product not found"));

        redisTemplate
        .opsForValue()
        .set(
                key,
                response,
                Duration.ofMinutes(10));

        return response;
    }

    // ---------------- SEARCH ----------------

    public Page<ProductResponse> getProductByName(
            String name,Pageable pageable) {

        return repository
                .findByNameContainingIgnoreCase(
                        name,pageable)
                .map(
                        ProductMapper::mapToResponse);
    }
}

//Query Flow
//
//Request
//↓
//Query Controller
//↓
//Query Service
//↓
//Repository
//↓
//Page<Product>
//↓
//map(ProductMapper::mapToResponse)
//↓
//Page<ProductResponse>
//↓
//API Response