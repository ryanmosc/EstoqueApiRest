package com.ryanmosc.ApiRestEstoque.controller;

import com.ryanmosc.ApiRestEstoque.exceptions.ProductError;
import com.ryanmosc.ApiRestEstoque.model.Exits;
import com.ryanmosc.ApiRestEstoque.model.Products;
import com.ryanmosc.ApiRestEstoque.service.ExitsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@RestController
@RequestMapping("/products/exit")
public class ExitsController {
    private  final ExitsService exitsService;

    //Create Exit
    @Operation
    @PostMapping
    public Exits saveExit (@RequestBody @Valid Exits exits){
        return exitsService.saveExit(exits);
    }

    //Read Exit
    @Operation
    @GetMapping
    public List<Exits> getAllExit(){
        return exitsService.getAllExit();
    }

    //Get Exit By id
    @Operation
    @GetMapping("/{id}")
    public ResponseEntity<Exits> findExitById(@PathVariable Long id){
        if (id == null || id <= 0){
            throw new ProductError("Id: " + id + " Inexistente");
        }
        Exits products =  exitsService.getExitById(id);
        return ResponseEntity.ok(products);
    }



    //Find Exit by Product id
    @Operation
    @GetMapping("/productName/{id}")
    public ResponseEntity<Products> findProductById(@PathVariable Long id){
        if (id == null || id <= 0){
            throw new ProductError("Id: " + id + " Inexistente");
        }
        Products products =  exitsService.findExitByProductId(id);
        return ResponseEntity.ok(products);
    }



}
