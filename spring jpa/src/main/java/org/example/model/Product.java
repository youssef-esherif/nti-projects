package org.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String sku;

    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private int stock;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Product() {}
    public Product(String sku, String name, BigDecimal price, int stock) {
        this.sku = sku; this.name = name; this.price = price; this.stock = stock;
    }

    public void addCategory(Category c) { categories.add(c); c.getProducts().add(this); }
    public void removeCategory(Category c) { categories.remove(c); c.getProducts().remove(this); }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public Set<Category> getCategories() { return categories; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return Objects.equals(sku, ((Product) o).sku);
    }
    @Override public int hashCode() { return Objects.hash(sku); }
}