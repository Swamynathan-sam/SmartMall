package com.smartmall.productservice.command.service;

import org.springframework.stereotype.Service;

import com.smartmall.productservice.command.entity.Category;
import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.entity.Review;
import com.smartmall.productservice.command.producer.ProductEventProducer;
import com.smartmall.productservice.command.repository.CategoryRepository;
import com.smartmall.productservice.command.repository.ProductRepository;
import com.smartmall.productservice.common.dto.ReviewRequest;
import com.smartmall.productservice.common.event.ProductCreatedEvent;
import com.smartmall.productservice.common.event.ProductUpdatedEvent;
import com.smartmall.productservice.common.event.ReviewAddedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository repository;
    
    private final CategoryRepository categoryRepository;

    private final ProductEventProducer producer;

    public Product createProduct(Product product) {
    	
    	 if(product.getCategory() != null){

    	        Category category =
    	                categoryRepository.findById(
    	                        product.getCategory().getId())
    	                .orElseThrow(() ->
    	                        new RuntimeException(
    	                                "Category not found"));

    	        product.setCategory(category);
    	    }
    	
    	product.setAverageRating(0.0);

        Product saved = repository.save(product);

        ProductCreatedEvent event =
                new ProductCreatedEvent(
                        //saved.getId(),
                		saved.getProductCode(),
                        saved.getName(),
                        saved.getDescription(),
                        saved.getPrice(),
                        saved.getQuantity(),
                        saved.getAverageRating());

        producer.publish(event);

        return saved;
    }
    
    public Product updateProduct(
            String productCode,
            Product updatedProduct) {

        Product existing =
                repository.findByProductCode(productCode)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"));

        existing.setName(updatedProduct.getName());

        existing.setDescription(
                updatedProduct.getDescription());

        existing.setPrice(
                updatedProduct.getPrice());

        existing.setQuantity(
                updatedProduct.getQuantity());
        
        if (updatedProduct.getCategory() != null) {

            Category category =
                    categoryRepository.findById(
                            updatedProduct.getCategory().getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Category not found"));

            existing.setCategory(category);
        }

        Product saved =
                repository.save(existing);

        ProductUpdatedEvent event =
                new ProductUpdatedEvent(
                        //saved.getId(),
                		saved.getProductCode(),
                        saved.getName(),
                        saved.getDescription(),
                        saved.getPrice(),
                        saved.getQuantity());

        producer.publish(event);

        return saved;
    }
    
    public Product addReview(
            String productCode,
            ReviewRequest request) {

        Product product =
                repository.findByProductCode(productCode)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"));

        Review review = new Review();

        review.setRating(request.getRating());

        review.setComment(
                request.getComment());

        review.setUsername(
                request.getUsername());

        review.setProduct(product);

        product.getReviews().add(review);

        double average =
                product.getReviews()
                        .stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);

        product.setAverageRating(average);

        Product saved =
                repository.save(product);

        ReviewAddedEvent event =
                new ReviewAddedEvent(
                        //saved.getId(),
                		saved.getProductCode(),
                		request.getRating(),
                		request.getComment(),
                		request.getUsername(),
                		saved.getAverageRating());

        producer.publish(event);

        return saved;
    }
    
    public Product getProductByCode(
            String productCode) {

        return repository.findByProductCode(
                productCode)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"));
    }
}