package com.smartmall.productservice.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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