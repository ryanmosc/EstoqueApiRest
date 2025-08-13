package com.ryanmosc.ApiRestEstoque.repository;

import com.ryanmosc.ApiRestEstoque.EntriesDto.GetAllEntriesDto;
import com.ryanmosc.ApiRestEstoque.model.Entries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntriesRepository extends JpaRepository  <Entries, Long>{
    @Query("""
        SELECT new com.ryanmosc.ApiRestEstoque.EntriesDto.GetAllEntriesDto(
            p.name, p.category, e.id, e.productId, e.date, e.quantity, e.type
        )
        FROM Entries e
        LEFT JOIN e.products p
    """)
    List<GetAllEntriesDto> findAllWithProductInfoE();

    @Query("""
        SELECT new com.ryanmosc.ApiRestEstoque.EntriesDto.GetAllEntriesDto(
            p.name, p.category, e.id, e.productId, e.date, e.quantity, e.type
        )
        FROM Entries e
        LEFT JOIN e.products p
        WHERE e.id = :id
    """)
    Optional<GetAllEntriesDto> findExitByIdWithProductInfo(@Param("id") Long id);
}
