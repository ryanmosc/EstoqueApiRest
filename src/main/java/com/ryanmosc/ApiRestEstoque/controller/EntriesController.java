package com.ryanmosc.ApiRestEstoque.controller;

import com.ryanmosc.ApiRestEstoque.EntriesDto.GetAllEntriesDto;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductError;
import com.ryanmosc.ApiRestEstoque.model.Entries;
import com.ryanmosc.ApiRestEstoque.service.EntriesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@RestController
@RequestMapping("/products/entries")

public class EntriesController {
    private final EntriesService entriesService;

    //Create Entrie
    @Operation
    @PostMapping
    public Entries saveExit(@RequestBody @Valid Entries entries) {
        return entriesService.saveEntries(entries);
    }

    //Read Entrie
    @Operation
    @GetMapping
    public List<GetAllEntriesDto> getAllEntries() {
        return entriesService.getAllEntriess();
    }

    //Get Entrie By id
    @Operation
    @GetMapping("/{id}")
    public ResponseEntity<GetAllEntriesDto> findEntriesById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new ProductError("Id: " + id + " Inexistente");
        }
        GetAllEntriesDto products = entriesService.getEntrieById(id);
        return ResponseEntity.ok(products);
    }
}
