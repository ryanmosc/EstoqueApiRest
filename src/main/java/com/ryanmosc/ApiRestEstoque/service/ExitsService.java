package com.ryanmosc.ApiRestEstoque.service;

import com.ryanmosc.ApiRestEstoque.ExitsDto.GetAllDto;
import com.ryanmosc.ApiRestEstoque.exceptions.*;
import com.ryanmosc.ApiRestEstoque.model.Enums;
import com.ryanmosc.ApiRestEstoque.model.Exits;
import com.ryanmosc.ApiRestEstoque.model.Products;
import com.ryanmosc.ApiRestEstoque.repository.ExitsRepository;
import com.ryanmosc.ApiRestEstoque.repository.ProductsRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Service
@Slf4j
public class ExitsService {

    private final ExitsRepository exitsRepository;
    private final ProductsRepository productsRepository;

    // Exists product
    public boolean findByIdBolean(Long id) {
        boolean exists = productsRepository.existsById(id);
        log.info("Verificando existência do produto com ID {}: {}", id, exists ? "Encontrado" : "Não encontrado");
        return exists;
    }

    // Find id Product
    public Products findById(Long id) {
        try {
            log.info("Buscando produto com ID {}", id);
            Products product = productsRepository.findById(id).orElseThrow(
                    () -> new ProductNotfound("Product with id: " + id + " not found"));
            log.info("Produto encontrado: {}", product.getName());
            return product;
        } catch (ProductNotfound e) {
            log.error("Produto com ID {} não encontrado", id, e);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar produto com ID {}", id, e);
            throw new ProductNotfound("Produto não encontrado com o id " + id);
        }
    }

    // Create Exit
    public Exits saveExit(Exits exit) {
        Long productId = exit.getProductId();
        log.info("Iniciando processo de saída para o produto com ID {}", productId);

        boolean existsId = findByIdBolean(productId);

        if (existsId) {
            Products products = findById(productId);
            Integer productQuantity = products.getQuantity();
            Integer exitQuantity = exit.getQuantity();

            log.info("Quantidade atual em estoque: {} | Quantidade solicitada para saída: {}", productQuantity, exitQuantity);

            if (exitQuantity > productQuantity) {
                log.error("Quantidade de saída ({}) maior que a disponível ({}) para o produto ID {}", exitQuantity, productQuantity, productId);
                throw new StockQuantityExceededException("Quantidade de saída: " + exitQuantity + " maior que a existente: " + productQuantity);
            } else {
                products.setQuantity(productQuantity - exitQuantity);
                productsRepository.save(products);

                exit.setType(String.valueOf(Enums.SAIDA));
                Exits savedExit = exitsRepository.saveAndFlush(exit);

                log.info("Saída registrada com sucesso para o produto ID {}. Nova quantidade em estoque: {}", productId, products.getQuantity());
                return savedExit;
            }
        } else {
            log.error("Tentativa de registrar saída para produto inexistente com ID {}", productId);
            throw new ProductNotfound();
        }
    }

    // Read Exits
    public List<GetAllDto> getAllExit() {
        try {
            log.info("Buscando todas as saídas registradas");
            List<GetAllDto> exits = exitsRepository.findAllWithProductInfo();
            log.info("Total de saídas encontradas: {}", exits.size());
            return exits;
        } catch (Exception e) {
            log.error("Erro ao buscar saídas", e);
            throw new ProductError("Erro ao buscar saídas");
        }
    }

    // Read Exit by id
    public GetAllDto getExitById(Long id) {
        try {
            log.info("Buscando saída com ID {}", id);
            GetAllDto exit = exitsRepository.findExitByIdWithProductInfo(id).orElseThrow(
                    () -> new ProductNotfound("Exit not found with id: " + id));
            log.info("Saída encontrada para o ID {}", id);
            return exit;
        } catch (ProductNotfound e) {
            log.error("Saída não encontrada com ID {}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar saída com ID {}", id, e);
            throw new ProductNotfound("Exit not found with id: " + id);
        }
    }
}
