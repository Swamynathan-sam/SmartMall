package com.smartmall.productservice.common.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent {

    //private Long id;
	private String productCode;

    private String name;

    private String description;

    private double price;

    private int quantity;

    private double averageRating;
}