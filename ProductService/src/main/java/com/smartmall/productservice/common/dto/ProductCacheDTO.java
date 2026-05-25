package com.smartmall.productservice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCacheDTO {

    private String productCode;

    private String name;

    private Double price;

    private Integer quantity;

    private Double averageRating;
}