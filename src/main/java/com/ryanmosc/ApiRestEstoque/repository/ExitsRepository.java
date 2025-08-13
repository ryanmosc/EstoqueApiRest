package com.ryanmosc.ApiRestEstoque.repository;

import com.ryanmosc.ApiRestEstoque.ExitsDto.GetAllDto;
import com.ryanmosc.ApiRestEstoque.model.Exits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExitsRepository extends JpaRepository<Exits, Long> {

    @Query("""
        SELECT new com.ryanmosc.ApiRestEstoque.ExitsDto.GetAllDto(
            p.name, p.category, e.id, e.productId, e.date, e.quantity, e.type
        )
        FROM Exits e
        LEFT JOIN e.products p
    """)
    List<GetAllDto> findAllWithProductInfo();

    @Query("""
        SELECT new com.ryanmosc.ApiRestEstoque.ExitsDto.GetAllDto(
            p.name, p.category, e.id, e.productId, e.date, e.quantity, e.type
        )
        FROM Exits e
        LEFT JOIN e.products p
        WHERE e.id = :id
    """)
    Optional<GetAllDto> findExitByIdWithProductInfo(@Param("id") Long id);
}