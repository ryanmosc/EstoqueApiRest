package com.ryanmosc.ApiRestEstoque.EntriesDto;

import java.time.LocalDate;

public record GetAllEntriesDto(
        String productName,
        String category,
        Long entriesId,
        Long productId,
        LocalDate date,
        Integer quantity,
        String type
) {}
