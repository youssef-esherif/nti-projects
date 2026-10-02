package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.example.model.Category;
import org.example.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    @PersistenceContext
    private EntityManager em;

    public Product save(Product p) {
        if (p.getId() == null) { em.persist(p); return p; }
        return em.merge(p);
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(em.find(Product.class, id));
    }

    public Optional<Product> findBySku(String sku) {
        return em.createQuery("select p from Product p where p.sku = :s", Product.class)
                .setParameter("s", sku)
                .getResultStream().findFirst();
    }

    public List<Product> findByCategory(String categoryName) {
        return em.createQuery(
                        "select p from Product p join p.categories c where c.name = :n", Product.class)
                .setParameter("n", categoryName)
                .getResultList();
    }

    public List<Product> findLowStock(int threshold) {
        return em.createQuery("select p from Product p where p.stock < :t", Product.class)
                .setParameter("t", threshold)
                .getResultList();
    }

    public List<Product> findPage(int page, int size) {
        return em.createQuery("select p from Product p order by p.id", Product.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public long countAll() {
        return em.createQuery("select count(p) from Product p", Long.class).getSingleResult();
    }

    public List<Product> search(String keyword, BigDecimal minPrice, BigDecimal maxPrice, String category) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        List<Predicate> preds = new ArrayList<>();
        if (keyword != null && !keyword.isBlank())
            preds.add(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        if (minPrice != null)
            preds.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        if (maxPrice != null)
            preds.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        if (category != null && !category.isBlank()) {
            Join<Product, Category> join = root.join("categories");
            preds.add(cb.equal(join.get("name"), category));
            cq.distinct(true);
        }
        cq.select(root).where(preds.toArray(new Predicate[0]));

        TypedQuery<Product> q = em.createQuery(cq);
        return q.getResultList();
    }
}