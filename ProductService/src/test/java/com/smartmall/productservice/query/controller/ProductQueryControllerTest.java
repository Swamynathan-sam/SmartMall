package com.smartmall.productservice.query.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.smartmall.productservice.common.dto.ProductResponse;
import com.smartmall.productservice.query.service.ProductQueryService;

@WebMvcTest(ProductQueryController.class)
class ProductQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductQueryService service;

    @Test
    void shouldGetProductByCode() throws Exception {

        ProductResponse response = new ProductResponse(
                1L, "P001", "Laptop", "Gaming Laptop",
                50000.0, 10,
                List.of("img1.jpg"),
                4.5, "Electronics"
        );

        when(service.getProductByCode("P001"))
                .thenReturn(response);

        mockMvc.perform(get("/queries/products/P001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void shouldSearchProducts() throws Exception {

        ProductResponse response = new ProductResponse(
                1L, "P001", "Laptop", "Gaming Laptop",
                50000.0, 10,
                List.of("img1.jpg"),
                4.5, "Electronics"
        );

        Page<ProductResponse> page =
                new PageImpl<>(List.of(response));

        when(service.getProductByName(eq("Laptop"), any()))
                .thenReturn(page);

        mockMvc.perform(get("/queries/products/search")
                        .param("name", "Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name")
                        .value("Laptop"));
    }
}