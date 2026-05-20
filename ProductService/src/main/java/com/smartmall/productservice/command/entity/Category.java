package com.smartmall.productservice.command.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,nullable=false)
    private String name;
    
    @OneToMany(mappedBy = "category")
    @JsonManagedReference("category-product")
    private List<Product> products;
}