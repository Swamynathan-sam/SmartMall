package com.smartmall.productservice.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmall.productservice.command.entity.Category;
import com.smartmall.productservice.command.service.CategoryService;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringJUnitConfig
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateCategory() throws Exception {

        Category category = new Category();

        category.setName("Electronics");

        when(service.createCategory(any(Category.class)))
                .thenReturn(category);

        mockMvc.perform(
                post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(category)
                        )
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name")
        .value("Electronics"));
    }
}