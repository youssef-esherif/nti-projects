package com.example.annotations;

public class ProductService {
    @Cacheable
    public String getProduct(String id) {
        System.out.println("TARGET: loading product " + id);
        if (id.equals("bad")) {
            throw new IllegalArgumentException("Product not found: " + id);
        }
        return "Product " + id;
    }
}
