package com.smartmall.productservice.common.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAddedEvent {

    private Long productId;

    private int rating;

    private String comment;

    private String username;

    private double averageRating;
}