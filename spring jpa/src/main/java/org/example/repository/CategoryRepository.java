package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    public Category save(Category c) {
        if (c.getId() == null) { em.persist(c); return c; }
        return em.merge(c);
    }
}