package com.smartmall.productservice.common.mapper;

import java.util.List;

import com.smartmall.productservice.command.entity.Product;
import com.smartmall.productservice.command.entity.ProductImage;
import com.smartmall.productservice.common.dto.ProductResponse;

public class ProductMapper {

    public static ProductResponse mapToResponse(
            Product product) {

        ProductResponse response =
                new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setAverageRating(
                product.getAverageRating());

        if(product.getCategory() != null) {
            response.setCategoryName(
                    product.getCategory().getName());
        }
        
        if(product.getImages() != null) {

            List<String> imageUrls =
                    product.getImages()
                            .stream()
                            .map(ProductImage::getImageUrl)
                            .toList();

            response.setImages(imageUrls);
        }

        return response;
    }
}