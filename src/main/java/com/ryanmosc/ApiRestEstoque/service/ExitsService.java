package com.ryanmosc.ApiRestEstoque.service;

import com.ryanmosc.ApiRestEstoque.exceptions.ProductBadRequests;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductNotfound;
import com.ryanmosc.ApiRestEstoque.model.Exits;
import com.ryanmosc.ApiRestEstoque.model.Products;
import com.ryanmosc.ApiRestEstoque.repository.ExitsRepository;
import com.ryanmosc.ApiRestEstoque.repository.ProductsRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

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
                    throw new RuntimeException("Quantidade de saida:" + productQuantity + " maior que a existente:" + exitQuantity);
                } else {
                    products.setQuantity(productQuantity);
                    productsRepository.save(products);
                    return exitsRepository.saveAndFlush(exit);
                }
            } else {
                throw new ProductNotfound();
            }

    }






}
