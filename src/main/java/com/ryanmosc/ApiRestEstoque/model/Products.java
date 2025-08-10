package com.ryanmosc.ApiRestEstoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "product_name")
    private String name;

    @NotBlank
    @Column(name = "category")
    private String category;

    @NotNull
    @Positive
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Positive
    @Column(name = "quantity")
    private Integer quantity;

    public Products(){}



}
