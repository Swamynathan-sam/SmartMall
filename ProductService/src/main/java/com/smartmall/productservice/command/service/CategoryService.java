package com.smartmall.productservice.command.service;

import com.smartmall.productservice.command.entity.Category;
import com.smartmall.productservice.command.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public Category createCategory(
            Category category) {

        return repository.save(category);
    }
}