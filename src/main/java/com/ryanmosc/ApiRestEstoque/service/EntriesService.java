package com.ryanmosc.ApiRestEstoque.service;

import com.ryanmosc.ApiRestEstoque.EntriesDto.GetAllEntriesDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Service
@Slf4j
public class EntriesService {

    private final EntriesRepository entriesRepository;
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

    // Create Entries
    public Entries saveEntries(Entries entries) {
        Long productId = entries.getProductId();
        log.info("Iniciando processo de entrada para o produto com ID {}", productId);

        boolean existsId = findByIdBolean(productId);

        if (existsId) {
            Products products = findById(productId);
            Integer productQuantity = products.getQuantity();
            Integer entryQuantity = entries.getQuantity();
            log.info("Quantidade atual em estoque: {} | Quantidade a adicionar: {}", productQuantity, entryQuantity);

            productQuantity += entryQuantity;

            if (productQuantity < 0) { // Aqui não faz muito sentido verificar negativo, mas mantive padrão
                log.error("Erro ao processar entrada: quantidade resultante negativa para produto ID {}", productId);
                throw new StockQuantityExceededException("Quantidade inválida após entrada");
            } else {
                products.setQuantity(productQuantity);
                productsRepository.save(products);
                entries.setType(String.valueOf(Enums.ENTRADA));
                Entries savedEntry = entriesRepository.saveAndFlush(entries);
                log.info("Entrada registrada com sucesso para produto ID {}. Nova quantidade em estoque: {}", productId, products.getQuantity());
                return savedEntry;
            }
        } else {
            log.error("Tentativa de registrar entrada para produto inexistente com ID {}", productId);
            throw new ProductNotfound();
        }
    }

    // Read Entries
    public List<GetAllEntriesDto> getAllEntriess() {
        try {
            log.info("Buscando todas as entradas registradas");
            List<GetAllEntriesDto> entriesList = entriesRepository.findAllWithProductInfoE();
            log.info("Total de entradas encontradas: {}", entriesList.size());
            return entriesList;
        } catch (Exception e) {
            log.error("Erro ao buscar entradas", e);
            throw new ProductError("Erro ao buscar entradas");
        }
    }

    // Read Entry by id
    public GetAllEntriesDto getEntrieById(Long id) {
        try {
            log.info("Buscando entrada com ID {}", id);
            GetAllEntriesDto entry = entriesRepository.findExitByIdWithProductInfo(id).orElseThrow(
                    () -> new ProductNotfound("Entry not found with id: " + id));
            log.info("Entrada encontrada para o ID {}", id);
            return entry;
        } catch (ProductNotfound e) {
            log.error("Entrada não encontrada com ID {}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao buscar entrada com ID {}", id, e);
            throw new ProductNotfound("Entry not found with id: " + id);
        }
    }
}
