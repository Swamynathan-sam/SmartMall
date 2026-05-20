package com.smartmall.productservice.command.repository;

import com.smartmall.productservice.command.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {
}