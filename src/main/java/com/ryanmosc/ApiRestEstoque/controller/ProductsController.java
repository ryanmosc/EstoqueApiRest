package com.ryanmosc.ApiRestEstoque.controller;


import com.ryanmosc.ApiRestEstoque.exceptions.ProductError;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductNotfound;
import com.ryanmosc.ApiRestEstoque.model.Products;
import com.ryanmosc.ApiRestEstoque.service.ProductsService;
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
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;

    //Create Products
    @Operation
    @PostMapping
    public Products saveProduct(@RequestBody @Valid Products product){
        return productsService.saveProduct(product);
    }

    //Read Products
    @Operation
    @GetMapping()
    public List<Products> getAllProducts(){
        return  productsService.getAll();
    }

    //list products with stock below 5
    @Operation
    @GetMapping("/lowStock")
    public List<Products> productsStockBelow5(){
        return   productsService.productsStockBelow5();
    }

    //Find Product by id
    @Operation
    @GetMapping("/{id}")
    public ResponseEntity<Products> findProductById(@PathVariable Long id){
        if (id == null || id <= 0){
            throw new ProductError("Id: " + id + " Inexistente");
        }
       Products products =  productsService.findById(id);
        return ResponseEntity.ok(products);
    }

    //Find by Product name
    @Operation
    @GetMapping(params = "name")
    public ResponseEntity<List<Products>> findByName(@RequestParam String name){
        if (name == null ||name.trim().isEmpty()){
            throw new ProductError("Product name has not found " + name);
        }
        List<Products> products = productsService.findByName(name);
        return ResponseEntity.ok(products) ;
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<Products> updateProduct(@RequestBody @Valid Products products, @PathVariable Long id){
        if (id == null || id <= 0){
            throw new ProductNotfound();
        }
        Products products1 = productsService.productUpdated(products, id);
        return  ResponseEntity.ok(products1);
    }

    //Delete Product
    @Operation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        if (id == null || id <= 0){
            throw new ProductNotfound();
        }
        productsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
