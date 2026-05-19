package com.smartmall.productservice.common.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdatedEvent {

    private Long id;

    private String name;

    private String description;

    private double price;

    private int quantity;
}