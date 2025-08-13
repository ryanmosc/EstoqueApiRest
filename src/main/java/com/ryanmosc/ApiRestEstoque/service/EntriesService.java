package com.ryanmosc.ApiRestEstoque.service;




import com.ryanmosc.ApiRestEstoque.EntriesDto.GetAllEntriesDto;
import com.ryanmosc.ApiRestEstoque.ExitsDto.GetAllDto;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductError;
import com.ryanmosc.ApiRestEstoque.exceptions.ProductNotfound;
import com.ryanmosc.ApiRestEstoque.exceptions.StockQuantityExceededException;
import com.ryanmosc.ApiRestEstoque.model.Entries;
import com.ryanmosc.ApiRestEstoque.model.Enums;
import com.ryanmosc.ApiRestEstoque.model.Products;
import com.ryanmosc.ApiRestEstoque.repository.EntriesRepository;
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
public class EntriesService {

    private final EntriesRepository entriesRepository;
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
        } catch (Exception e) {
            throw new ProductNotfound("Produto não encontrado com o id " + id);
        }
    }


    //Create Entries
    public Entries saveEntries(Entries entries) {

        Long productId = entries.getProductId();
        boolean existsId = findByIdBolean(productId);

        if (existsId) {
            Products products = findById(productId);
            Integer productQuantity = products.getQuantity();
            Integer exitQuantity = entries.getQuantity();
            productQuantity += exitQuantity;

            if (productQuantity < exitQuantity) {
                throw new StockQuantityExceededException("Quantidade de saida:" + productQuantity + " maior que a existente:" + exitQuantity);
            } else {
                products.setQuantity(productQuantity);
                productsRepository.save(products);
                entries.setType(String.valueOf(Enums.ENTRADA));
                return entriesRepository.saveAndFlush(entries);
            }
        } else {
            throw new ProductNotfound();
        }

    }

    //Read Entrie
    public List<GetAllEntriesDto> getAllEntriess() {
        try {
            return entriesRepository.findAllWithProductInfoE();
        } catch (Exception e) {
            throw new ProductError("Erro ao buscar saidas");
        }
    }

    //Read Entrie by id
    public GetAllEntriesDto getEntrieById(Long id) {
        try {
            return entriesRepository.findExitByIdWithProductInfo(id).orElseThrow(
                    () -> new ProductNotfound("Exit not found with id: " + id));
        } catch (Exception e) {
            throw new ProductNotfound("Exit not found with id: " + id);
        }
    }
}
