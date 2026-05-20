package com.smartmall.productservice.common.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAddedEvent {

    private String productCode;

    private int rating;

    private String comment;

    private String username;

    private double averageRating;
}