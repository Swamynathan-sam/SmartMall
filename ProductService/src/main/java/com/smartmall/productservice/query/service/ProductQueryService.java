package com.smartmall.productservice.query.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductQueryService {


private final RedisTemplate<String, Object> redisTemplate;

private final ProductRepository repository;

public Product getProduct(Long id) {

    Object cachedProduct =
            redisTemplate.opsForValue()
                    .get("product:" + id);

    if (cachedProduct != null) {

        return (Product) cachedProduct;
    }

    Product product = repository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Product not found"));

    redisTemplate.opsForValue()
            .set("product:" + id, product);

    return product;
}

public List<Product> getProductByName(
        String name) {

    return repository
            .findByNameContainingIgnoreCase(name);
}


}
