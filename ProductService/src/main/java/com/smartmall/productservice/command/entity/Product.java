package com.smartmall.productservice.command.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    
    @Column(unique = true, nullable= false)
    private String productCode;

    private String name;

    @Column(length = 2000)
    private String description;

    private Double price;

    private Integer quantity;

    @Column(nullable = false)
    private Double averageRating =0.0;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference("category-product")
    private Category category;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference("product-images")
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference("product-reviews")
    private List<Review> reviews = new ArrayList<>();
    
    @PrePersist
    public void generateProductCode() {

        if (this.productCode == null) {

            this.productCode =
                    "PRD-" +
                    UUID.randomUUID()
                            .toString()
                            .substring(0,8)
                            .toUpperCase();
        }
    }
}