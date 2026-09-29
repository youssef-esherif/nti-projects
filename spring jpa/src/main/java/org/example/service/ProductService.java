package org.example.service;

import org.example.exception.NotFoundException;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) { this.repo = repo; }

    @Transactional
    public Product addProduct(String sku, String name, BigDecimal price, int stock) {
        return repo.save(new Product(sku, name, price, stock));
    }

    @Transactional
    public void restock(Long productId, int quantity) {
        Product p = repo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        p.setStock(p.getStock() + quantity);
        // dirty checking
    }

    @Transactional
    public void changePrice(Long productId, BigDecimal newPrice) {
        Product p = repo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        p.setPrice(newPrice);
    }
}