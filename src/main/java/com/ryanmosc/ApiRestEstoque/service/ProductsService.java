package com.ryanmosc.ApiRestEstoque.service;

import com.ryanmosc.ApiRestEstoque.exceptions.*;
import com.ryanmosc.ApiRestEstoque.model.Products;
import com.ryanmosc.ApiRestEstoque.repository.ProductsRepository;
import jakarta.transaction.Transactional;
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
public class ProductsService {

    private final ProductsRepository productsRepository;

    // Create Product
    public Products saveProduct(Products products) {
        log.info("Iniciando salvamento do produto: {}", products.getName());
        try {
            Products saved = productsRepository.saveAndFlush(products);
            log.info("Produto salvo com sucesso. ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Erro ao inserir produto no banco de dados: {}", products.getName(), e);
            throw new ProductBadRequests("Erro ao inserir produto no banco de dados");
        }
    }

    // Read Products
    public List<Products> getAll() {
        log.info("Buscando todos os produtos...");
        try {
            List<Products> products = productsRepository.findAll();
            log.info("Total de produtos encontrados: {}", products.size());
            return products;
        } catch (Exception e) {
            log.error("Erro ao buscar produtos", e);
            throw new ProductError("Erro ao buscar produtos");
        }
    }

    // List products with stock below 5
    public List<Products> productsStockBelow5() {
        log.info("Buscando produtos com estoque menor ou igual a 5...");
        try {
            List<Products> products = productsRepository.findByQuantityLessThanEqual(5);
            if (products.isEmpty()) {
                log.warn("Nenhum produto com estoque baixo encontrado");
                throw new ProductBadRequests("Não há produtos com estoque baixo");
            }
            log.info("Produtos com estoque baixo encontrados: {}", products.size());
            return products;
        } catch (Exception e) {
            log.error("Erro ao buscar produtos com estoque baixo", e);
            throw new ProductBadRequests("Não há produtos com estoque baixo");
        }
    }

    // Find Product by id
    public Products findById(Long id) {
        log.info("Buscando produto com ID: {}", id);
        try {
            Products product = productsRepository.findById(id)
                    .orElseThrow(() -> new ProductNotfound("Produto não encontrado com o ID " + id));
            log.info("Produto encontrado: {}", product.getName());
            return product;
        } catch (Exception e) {
            log.error("Erro ao buscar produto com ID: {}", id, e);
            throw new ProductNotfound("Produto não encontrado com o ID " + id);
        }
    }

    // Find by Product name
    public List<Products> findByName(String name) {
        log.info("Buscando produtos pelo nome: {}", name);
        List<Products> products = productsRepository.findByNameContainingIgnoreCase(name);
        if (products.isEmpty()) {
            log.warn("Nenhum produto encontrado com o nome: {}", name);
            throw new ProductNameNotFound("Nenhum produto encontrado com o nome: " + name);
        }
        log.info("Produtos encontrados com o nome '{}': {}", name, products.size());
        return products;
    }

    // Update
    public Products productUpdated(Products products, Long id) {
        log.info("Iniciando atualização do produto com ID: {}", id);
        try {
            Products existing = findById(id);
            existing.setName(products.getName());
            existing.setCategory(products.getCategory());
            existing.setPrice(products.getPrice());
            existing.setQuantity(products.getQuantity());

            Products updated = productsRepository.saveAndFlush(existing);
            log.info("Produto atualizado com sucesso. ID: {}", updated.getId());
            return updated;
        } catch (ProductNotfound e) {
            log.warn("Tentativa de atualizar produto inexistente. ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao atualizar produto com ID: {}", id, e);
            throw new ProductBadRequests("Erro ao atualizar produto");
        }
    }

    // Delete
    @Transactional
    public void delete(Long id) {
        log.info("Iniciando exclusão do produto com ID: {}", id);
        try {
            Products existing = findById(id);
            productsRepository.deleteById(existing.getId());
            log.info("Produto excluído com sucesso. ID: {}", id);
        } catch (ProductNotfound e) {
            log.warn("Tentativa de excluir produto inexistente. ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao excluir produto com ID: {}", id, e);
            throw new ProductError("Erro ao deletar produto");
        }
    }
}
