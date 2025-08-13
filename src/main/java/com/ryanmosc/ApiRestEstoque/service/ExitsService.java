package com.ryanmosc.ApiRestEstoque.service;

import com.ryanmosc.ApiRestEstoque.exceptions.*;
import com.ryanmosc.ApiRestEstoque.model.Exits;
import com.ryanmosc.ApiRestEstoque.model.Products;
import com.ryanmosc.ApiRestEstoque.repository.ExitsRepository;
import com.ryanmosc.ApiRestEstoque.repository.ProductsRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Service
public class ExitsService {
    private final ExitsRepository exitsRepository;
    private final ProductsRepository productsRepository;

    //Exists product
    public boolean findByIdBolean(Long id) {
        return productsRepository.existsById(id);
    }

    //Find id Product
    public Products findById(Long id) {
        try {
            return productsRepository.findById(id).orElseThrow(
                    () -> new ProductNotfound("product with id:" + id + " not found"));
        }
        catch (Exception e){
            throw new ProductNotfound("Produto não encontrado com o id " + id);
        }
    }




    //Create Exit
    public Exits saveExit (Exits exit){

            Long productId = exit.getProductId();
            boolean existsId = findByIdBolean(productId);

            if (existsId) {
                Products products = findById(productId);
                Integer productQuantity = products.getQuantity();
                Integer exitQuantity = exit.getQuantity();
                productQuantity -= exitQuantity;

                if (productQuantity < exitQuantity) {
                    throw new StockQuantityExceededException("Quantidade de saida:" + productQuantity + " maior que a existente:" + exitQuantity);
                } else {
                    products.setQuantity(productQuantity);
                    productsRepository.save(products);
                    return exitsRepository.saveAndFlush(exit);
                }
            } else {
                throw new ProductNotfound();
            }

    }

    //Read Exits
    public List<Exits> getAllExit(){
        try {
            return exitsRepository.findAll();
        }
        catch (Exception e){
            throw new ProductError("Erro ao buscar saidas");
        }
    }

    //Read Exit by id
    public Exits getExitById(Long id){
        try {
            return exitsRepository.findById(id).orElseThrow(
                    () -> new ProductNotfound("Exit not found with id: " + id));
        }
        catch (Exception e){
            throw new ProductNotfound("Exit not found with id: " + id);
        }


    }



    //Find Product by id
    public Products findExitByProductId(Long id) {
        try {
            return productsRepository.findById(id).orElseThrow(
                    () -> new ProductNotfound("product with id:" + id + " not found"));
        }
        catch (Exception e){
            throw new ProductNotfound("Produto não encontrado com o id " + id);
        }
    }







}
