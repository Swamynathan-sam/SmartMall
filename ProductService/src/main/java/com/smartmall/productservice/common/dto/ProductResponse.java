package com.smartmall.productservice.common.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    
    private String productCode;

    private String name;

    private String description;

    private Double price;
    
    private Integer quantity;

    private List<String> images;

    private Double averageRating;

    private String categoryName;
}