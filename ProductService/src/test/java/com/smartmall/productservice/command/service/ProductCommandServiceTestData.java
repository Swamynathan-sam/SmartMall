package com.smartmall.productservice.command.service;

import com.smartmall.productservice.command.entity.Category;
import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.common.dto.ReviewRequest;

public class ProductCommandServiceTestData {

    public static Product buildProduct() {
        Product p = new Product();
        p.setProductCode("P001");
        p.setName("Laptop");
        p.setDescription("Gaming Laptop");
        p.setPrice(50000.0);
        p.setQuantity(10);
        return p;
    }

    public static Category buildCategory() {
        Category c = new Category();
        c.setId(1L);
        c.setName("Electronics");
        return c;
    }

    public static ReviewRequest buildReview() {
        ReviewRequest r = new ReviewRequest();
        r.setRating(5);
        r.setComment("Good product");
        r.setUsername("user1");
        return r;
    }
}