package com.ryanmosc.ApiRestEstoque.service;

import com.ryanmosc.ApiRestEstoque.exceptions.ProductBadRequests;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductError;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductNameNotFound;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductNotfound;
import com.ryanmosc.ApiRestEstoque.model.Products;
import com.ryanmosc.ApiRestEstoque.repository.ProductsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    //Create Product
    public Products saveProduct (Products products){
        try {
            return productsRepository.saveAndFlush(products);
        }
        catch (Exception e){
            throw new ProductBadRequests("Erro ao inserir Produto no banco de dados");
        }
    }

    //Reade Products
    public List<Products> getAll(){
        try {
            return productsRepository.findAll();
        }
        catch (Exception e){
            throw new ProductError("Erro ao buscar produtos");
        }
    }

    //list products with stock below 5
    public List<Products> productsStockBelow5() {
        try {
            List<Products> products = productsRepository.findByQuantityLessThanEqual(5);
            if (products.isEmpty()) {
                throw new ProductBadRequests("Não há produtos com estoque baixo");
            }
            return products;

        } catch (Exception e) {
            throw new ProductBadRequests("Não há produtos com estoque baixo");
        }
    }

    //Find Product by id
    public Products findById(Long id) {
        try {
            return productsRepository.findById(id).orElseThrow(
                    () -> new ProductNotfound("product with id:" + id + " not found"));
        }
        catch (Exception e){
            throw new ProductNotfound("Produto não encontrado com o id " + id);
        }
    }

    //Find by Product name
    public List<Products> findByName(String name){
        List<Products> products = productsRepository.findByNameContainingIgnoreCase(name);
        if (products.isEmpty()) {
            throw new ProductNameNotFound("No products found with name: " + name);
        }
        return products;
    }

    //Update
    public Products productUpdated(Products products, Long id){
        try {
            Products productsId = findById(id);
            if (productsId != null) {
                productsId.setName(products.getName());
                productsId.setCategory(products.getCategory());
                productsId.setPrice(products.getPrice());
                productsId.setQuantity(products.getQuantity());
                return productsRepository.saveAndFlush(productsId);
            }
            else {
                throw new ProductNotfound("Produto não encontrado com o id " + id);
            }
        }
        catch (Exception e){
            throw new ProductBadRequests();
        }
    }

    //Delete
    @Transactional
    public void delete(Long id){
        try {
            Products find_id = findById(id);
            if (find_id != null) {
                productsRepository.deleteById(id);
            }
            else {
                throw new ProductNotfound("Produto não encontrado com o id " + id);
            }
        }
        catch (Exception e){
            throw new ProductError("Erro ao deletar...");
        }
    }



}
