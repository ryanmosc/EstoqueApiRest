package com.ryanmosc.ApiRestEstoque.ExitsDto;

import java.time.LocalDate;

public record GetAllDto(
        String productName,
        String category,
        Long exitId,
        Long productId,
        LocalDate date,
        Integer quantity,
        String type
) {}
