package com.smartmall.productservice.command.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    private double price;

    private int quantity;

    private double averageRating;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<ProductImage> images;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Review> reviews;
}