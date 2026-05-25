package com.smartmall.productservice.command.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.service.ProductCommandService;
import com.smartmall.productservice.common.dto.ReviewRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductCommandControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ProductCommandService service;

    @InjectMocks
    private ProductCommandController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    // ================= CREATE =================

    @Test
    void shouldCreateProduct() throws Exception {

        Product product = buildProduct();

        when(service.createProduct(any()))
                .thenReturn(product);

        mockMvc.perform(post("/commands/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(service).createProduct(any());
    }

    // ================= GET =================

    @Test
    void shouldGetProduct() throws Exception {

        when(service.getProductByCode("P100"))
                .thenReturn(buildProduct());

        mockMvc.perform(get("/commands/products/P100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productCode").value("P100"));

        verify(service).getProductByCode("P100");
    }

    // ================= UPDATE =================

    @Test
    void shouldUpdateProduct() throws Exception {

        Product updated = buildProduct();
        updated.setName("Updated Laptop");

        when(service.updateProduct(eq("P100"), any()))
                .thenReturn(updated);

        mockMvc.perform(put("/commands/products/P100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"));

        verify(service).updateProduct(eq("P100"), any());
    }

    // ================= REVIEW =================

    @Test
    void shouldAddReview() throws Exception {

        ReviewRequest request = new ReviewRequest();
        request.setRating(5);
        request.setComment("Good");
        request.setUsername("user1");

        when(service.addReview(eq("P100"), any()))
                .thenReturn(buildProduct());

        mockMvc.perform(post("/commands/products/P100/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service).addReview(eq("P100"), any());
    }

    // ================= IMAGE =================

    @Test
    void shouldUploadImage() throws Exception {

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "test.jpg",
                        "image/jpeg",
                        "dummy".getBytes()
                );

        when(service.addImage(eq("P100"), any()))
                .thenReturn(buildProduct());

        mockMvc.perform(multipart("/commands/products/P100/images")
                .file(file))
                .andExpect(status().isOk());

        verify(service).addImage(eq("P100"), any());
    }

    // ================= TEST DATA =================

    private Product buildProduct() {
        Product p = new Product();
        p.setProductCode("P100");
        p.setName("Laptop");
        p.setPrice(50000.0);
        p.setQuantity(10);
        return p;
    }
}