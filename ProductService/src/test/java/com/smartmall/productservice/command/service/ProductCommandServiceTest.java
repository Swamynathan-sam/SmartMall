package com.smartmall.productservice.command.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.smartmall.productservice.command.entity.Category;
import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.producer.ProductEventProducer;
import com.smartmall.productservice.command.repository.CategoryRepository;
import com.smartmall.productservice.command.repository.ProductRepository;
import com.smartmall.productservice.common.dto.ReviewRequest;
import com.smartmall.productservice.common.event.ProductCreatedEvent;
import com.smartmall.productservice.common.event.ProductUpdatedEvent;
import com.smartmall.productservice.common.event.ReviewAddedEvent;

@ExtendWith(MockitoExtension.class)
class ProductCommandServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductEventProducer producer;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProductCommandService service;

    // ================= CREATE PRODUCT =================

    @Test
    void shouldCreateProduct() {

        Product product = ProductCommandServiceTestData.buildProduct();
        Category category = ProductCommandServiceTestData.buildCategory();
        product.setCategory(category);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(repository.save(any(Product.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Product result = service.createProduct(product);

        ArgumentCaptor<ProductCreatedEvent> captor =
                ArgumentCaptor.forClass(ProductCreatedEvent.class);

        verify(producer).publish(captor.capture());

        ProductCreatedEvent event = captor.getValue();

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Laptop", result.getName()),
                () -> assertEquals(0.0, result.getAverageRating()),
                () -> assertEquals("Laptop", event.getName())
        );
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {

        Product product = ProductCommandServiceTestData.buildProduct();
        product.setCategory(ProductCommandServiceTestData.buildCategory());

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.createProduct(product));

        assertEquals("Category not found", ex.getMessage());

        verify(repository, never()).save(any());
        verify(producer, never()).publish(any());
    }

    // ================= UPDATE PRODUCT =================

    @Test
    void shouldUpdateProduct() {

        Product existing = ProductCommandServiceTestData.buildProduct();

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.of(existing));

        when(repository.save(any(Product.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Product update = ProductCommandServiceTestData.buildProduct();
        update.setName("Updated Laptop");

        Product result = service.updateProduct("P001", update);

        ArgumentCaptor<ProductUpdatedEvent> captor =
                ArgumentCaptor.forClass(ProductUpdatedEvent.class);

        verify(producer).publish(captor.capture());

        ProductUpdatedEvent event = captor.getValue();

        assertAll(
                () -> assertEquals("Updated Laptop", result.getName()),
                () -> assertEquals("P001", event.getProductCode())
        );
    }
    
    @Test
    void shouldUpdateProductWithCategoryChange() {

        Product existing = ProductCommandServiceTestData.buildProduct();

        Category newCategory = new Category();
        newCategory.setId(2L);

        Product update = ProductCommandServiceTestData.buildProduct();
        update.setName("Updated Laptop");
        update.setCategory(newCategory); // 🔥 THIS triggers the IF block

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.of(existing));

        when(categoryRepository.findById(2L))
                .thenReturn(Optional.of(newCategory));

        when(repository.save(any(Product.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Product result = service.updateProduct("P001", update);

        assertEquals("Updated Laptop", result.getName());
        assertEquals(newCategory, result.getCategory());

        verify(categoryRepository).findById(2L);
    }
    @Test
    void shouldUpdateProductWithCategory() {

        Product existing = ProductCommandServiceTestData.buildProduct();

        Category newCategory = ProductCommandServiceTestData.buildCategory();
        newCategory.setId(2L);

        Product update = ProductCommandServiceTestData.buildProduct();
        update.setName("Updated Laptop");
        update.setCategory(newCategory);   // 🔥 IMPORTANT LINE

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.of(existing));

        when(categoryRepository.findById(2L))
                .thenReturn(Optional.of(newCategory));  // 🔥 MOCK CATEGORY

        when(repository.save(any(Product.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Product result = service.updateProduct("P001", update);

        ArgumentCaptor<ProductUpdatedEvent> captor =
                ArgumentCaptor.forClass(ProductUpdatedEvent.class);

        verify(producer).publish(captor.capture());

        ProductUpdatedEvent event = captor.getValue();

        assertAll(
                () -> assertEquals("Updated Laptop", result.getName()),
                () -> assertEquals("P001", event.getProductCode()),
                () -> assertEquals(newCategory, result.getCategory()) // 🔥 COVERED LINE
        );
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundInUpdate() {

        Product existing = ProductCommandServiceTestData.buildProduct();

        Product update = ProductCommandServiceTestData.buildProduct();
        update.setCategory(ProductCommandServiceTestData.buildCategory());

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.of(existing));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.updateProduct("P001", update));

        assertEquals("Category not found", ex.getMessage());

        verify(repository).findByProductCode("P001");
        verify(categoryRepository).findById(1L);
        verify(repository, never()).save(any());
        verify(producer, never()).publish(any());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundInCreateProduct() {

        Product product = new Product();

        Category category = new Category();
        category.setId(1L);

        product.setCategory(category); // IMPORTANT → enters IF block

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.createProduct(product));

        assertEquals("Category not found", ex.getMessage());

        verify(repository, never()).save(any());
        verify(producer, never()).publish(any());
    }
    // ================= ADD REVIEW =================

    @Test
    void shouldAddReview() {

        Product product = ProductCommandServiceTestData.buildProduct();

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.of(product));

        when(repository.save(any(Product.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ReviewRequest request =
                ProductCommandServiceTestData.buildReview();

        Product result = service.addReview("P001", request);

        ArgumentCaptor<ReviewAddedEvent> captor =
                ArgumentCaptor.forClass(ReviewAddedEvent.class);

        verify(producer).publish(captor.capture());

        ReviewAddedEvent event = captor.getValue();

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Laptop", result.getName()),
                () -> assertTrue(result.getReviews().size() > 0),
                () -> assertEquals("user1", event.getUsername())
        );
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundForReview() {

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.empty());

        ReviewRequest request =
                ProductCommandServiceTestData.buildReview();

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.addReview("P001", request));

        assertEquals("Product not found", ex.getMessage());

        verify(repository, never()).save(any());
        verify(producer, never()).publish(any());
    }

    // ================= GET PRODUCT =================

    @Test
    void shouldGetProductByCode() {

        Product product = ProductCommandServiceTestData.buildProduct();

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.of(product));

        Product result = service.getProductByCode("P001");

        assertEquals("Laptop", result.getName());

        verify(repository).findByProductCode("P001");
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundForGet() {

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.getProductByCode("P001"));

        assertEquals("Product not found", ex.getMessage());
    }

    // ================= ADD IMAGE =================

    @Test
    void shouldAddImage() throws Exception {

        Product product = ProductCommandServiceTestData.buildProduct();

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.of(product));

        when(fileStorageService.uploadFile(any()))
                .thenReturn("image.jpg");

        when(repository.save(any(Product.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        MultipartFile file = mock(MultipartFile.class);

        Product result = service.addImage("P001", file);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Laptop", result.getName())
        );

        verify(fileStorageService).uploadFile(any());
        verify(repository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundForImage() throws IOException {

        when(repository.findByProductCode("P001"))
                .thenReturn(Optional.empty());

        MultipartFile file = mock(MultipartFile.class);

        RuntimeException ex =
                assertThrows(RuntimeException.class,
                        () -> service.addImage("P001", file));

        assertEquals("Product not found", ex.getMessage());

        verify(fileStorageService, never()).uploadFile(any());
        verify(repository, never()).save(any());
    }
}