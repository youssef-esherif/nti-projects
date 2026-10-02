package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.model.AuditLog;
import org.springframework.stereotype.Repository;

@Repository
public class AuditLogRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(AuditLog log) { em.persist(log); }
}