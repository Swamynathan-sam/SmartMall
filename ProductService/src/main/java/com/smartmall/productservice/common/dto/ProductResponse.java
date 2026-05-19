package com.smartmall.productservice.common.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private double price;

    private List<String> images;

    private double averageRating;

    private String categoryName;
}