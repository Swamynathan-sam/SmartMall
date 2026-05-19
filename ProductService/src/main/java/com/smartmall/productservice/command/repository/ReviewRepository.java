package com.smartmall.productservice.command.repository;

import com.smartmall.productservice.command.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {
}