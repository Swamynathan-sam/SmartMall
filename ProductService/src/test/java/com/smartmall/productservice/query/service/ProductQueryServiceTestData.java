package com.smartmall.productservice.query.service;

import com.smartmall.productservice.common.dto.ProductResponse;

import java.util.List;

public class ProductQueryServiceTestData {

    public static ProductResponse sampleProductResponse() {
        return new ProductResponse(
                1L,
                "P001",
                "Laptop",
                "Gaming Laptop",
                50000.0,
                10,
                List.of("img1.jpg"),
                4.5,
                "Electronics"
        );
    }
}