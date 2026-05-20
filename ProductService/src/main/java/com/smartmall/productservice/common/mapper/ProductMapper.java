package com.smartmall.productservice.common.mapper;

import java.util.Collections;
import java.util.List;

import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.entity.ProductImage;
import com.smartmall.productservice.common.dto.ProductResponse;

public class ProductMapper {

    public static ProductResponse mapToResponse(
            Product product) {

        ProductResponse response =
                new ProductResponse();

        response.setId(
                product.getId());

        response.setProductCode(
                product.getProductCode());

        response.setName(
                product.getName());

        response.setDescription(
                product.getDescription());

        response.setPrice(
                product.getPrice());

        response.setQuantity(
                product.getQuantity());

        response.setAverageRating(
                product.getAverageRating());

        if(product.getCategory() != null) {

            response.setCategoryName(
                    product.getCategory().getName());
        }

        List<String> imageUrls =
                product.getImages() != null
                ? product.getImages()
                    .stream()
                    .map(ProductImage::getImageUrl)
                    .toList()
                : Collections.emptyList();

        response.setImages(
                imageUrls);

        return response;
    }
}