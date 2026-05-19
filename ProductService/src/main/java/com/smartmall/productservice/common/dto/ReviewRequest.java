package com.smartmall.productservice.common.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    private int rating;

    private String comment;

    private String username;
}