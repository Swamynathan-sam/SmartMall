package com.smartmall.productservice.query.service;

import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.repository.ProductRepository;
import com.smartmall.productservice.common.dto.ProductResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceTest {

    @Mock
    private RedisTemplate<String, ProductResponse> redisTemplate;

    @Mock
    private ValueOperations<String, ProductResponse> valueOperations;

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductQueryService service;

    // ---------- CACHE HIT ----------
    @Test
    void shouldReturnProductFromCache() {

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        ProductResponse cached = ProductQueryServiceTestData.sampleProductResponse();

        when(valueOperations.get("productCode:P001"))
                .thenReturn(cached);

        ProductResponse result = service.getProductByCode("P001");

        assertNotNull(result);
        assertEquals("Laptop", result.getName());

        verify(repository, never()).findResponseByProductCode(any());
    }

    // ---------- DB HIT ----------
    @Test
    void shouldReturnProductFromDatabase() {

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("productCode:P001")).thenReturn(null);

        ProductResponse dbResponse = ProductQueryServiceTestData.sampleProductResponse();

        when(repository.findResponseByProductCode("P001"))
                .thenReturn(Optional.of(dbResponse));

        ProductResponse result = service.getProductByCode("P001");

        assertEquals("Laptop", result.getName());

        verify(repository).findResponseByProductCode("P001");
        verify(valueOperations)
                .set(eq("productCode:P001"), any(), any());
    }

    // ---------- SEARCH ----------
    @Test
    void shouldSearchProducts() {

        Product product = new Product();
        product.setName("Laptop");

        Page<Product> page = new PageImpl<>(List.of(product));

        Pageable pageable = PageRequest.of(0, 5);

        when(repository.findByNameContainingIgnoreCase(
                eq("Laptop"),
                eq(pageable)))
                .thenReturn(page);

        Page<ProductResponse> result =
                service.getProductByName("Laptop", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Laptop", result.getContent().get(0).getName());
    }
}