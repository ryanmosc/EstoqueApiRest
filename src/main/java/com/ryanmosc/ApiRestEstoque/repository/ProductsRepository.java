package com.ryanmosc.ApiRestEstoque.repository;

import com.ryanmosc.ApiRestEstoque.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository <Products, Long> {
    List<Products> findByNameContainingIgnoreCase(String name);

    List<Products> findByQuantityLessThanEqual(Integer quantity);
}
