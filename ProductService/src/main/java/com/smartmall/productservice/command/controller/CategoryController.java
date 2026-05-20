package com.smartmall.productservice.command.controller;

import com.smartmall.productservice.command.entity.Category;
import com.smartmall.productservice.command.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public Category create(
            @RequestBody Category category) {

        return service.createCategory(category);
    }
}