package com.smartmall.productservice.command.service;

import com.smartmall.productservice.command.entity.Category;
import com.smartmall.productservice.command.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldCreateCategory() {

        Category category = new Category();
        category.setName("Electronics");

        when(repository.save(any(Category.class)))
                .thenReturn(category);

        Category savedCategory =
                categoryService.createCategory(category);

        assertNotNull(savedCategory);

        assertEquals(
                "Electronics",
                savedCategory.getName()
        );

        verify(repository, times(1))
                .save(any(Category.class));
    }
}