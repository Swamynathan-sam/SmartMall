package com.smartmall.productservice.command.service;

import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.entity.Review;
import com.smartmall.productservice.command.producer.ProductEventProducer;

import com.smartmall.productservice.command.repository.ProductRepository;
import com.smartmall.productservice.command.repository.ReviewRepository;
import com.smartmall.productservice.common.dto.ReviewRequest;
import com.smartmall.productservice.common.event.ProductCreatedEvent;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository repository;

    private final ProductEventProducer producer;
    
    private final ReviewRepository reviewRepository;

    public Product createProduct(Product product) {

        Product saved = repository.save(product);

        ProductCreatedEvent event =
                new ProductCreatedEvent(
                        saved.getId(),
                        saved.getName(),
                        saved.getDescription(),
                        saved.getPrice(),
                        saved.getQuantity(),
                        saved.getAverageRating());

        producer.publish(event);

        return saved;
    }
    
    public Product updateProduct(
            Long id,
            Product updatedProduct) {

        Product existing =
                repository.findById(id)
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

        Product saved =
                repository.save(existing);

        ProductCreatedEvent event =
                new ProductCreatedEvent(
                        saved.getId(),
                        saved.getName(),
                        saved.getDescription(),
                        saved.getPrice(),
                        saved.getQuantity(),
                        saved.getAverageRating());

        producer.publish(event);

        return saved;
    }
    
    public Product addReview(
            Long productId,
            ReviewRequest request) {

        Product product =
                repository.findById(productId)
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

        reviewRepository.save(review);

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

        ProductCreatedEvent event =
                new ProductCreatedEvent(
                        saved.getId(),
                        saved.getName(),
                        saved.getDescription(),
                        saved.getPrice(),
                        saved.getQuantity(),
                        saved.getAverageRating());

        producer.publish(event);

        return saved;
    }
}