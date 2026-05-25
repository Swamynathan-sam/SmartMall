package com.smartmall.productservice.common.mapper;

import java.util.Collections;
import java.util.List;
import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.entity.ProductImage;
import com.smartmall.productservice.common.dto.ProductResponse;

public class ProductMapper {

    public static ProductResponse mapToResponse(
            Product product) {

        List<String> imageUrls =
                product.getImages() != null
                ? product.getImages()
                        .stream()
                        .map(ProductImage::getImageUrl)
                        .toList()
                : Collections.emptyList();

        return new ProductResponse(
                product.getId(),
                product.getProductCode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                imageUrls,
                product.getAverageRating(),
                product.getCategory() != null
                        ? product.getCategory().getName()
                        : null
        );
    }
}