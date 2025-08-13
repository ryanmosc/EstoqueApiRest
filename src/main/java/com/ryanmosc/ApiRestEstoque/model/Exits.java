package com.ryanmosc.ApiRestEstoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Exits")
public class Exits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    @Column(name = "product_Id")
    private Long productId;

    @NotBlank
    @Column(name = "type")
    private String type;

    @Positive
    @Column(name = "quantity")
    private Integer quantity;


    @NotNull
    @Column(name = "date")
    private LocalDate date;

    public Exits(){}


}
