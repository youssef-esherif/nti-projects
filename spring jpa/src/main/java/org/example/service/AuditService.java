package org.example.service;

import org.example.model.AuditLog;
import org.example.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditLogRepository repo;

    public AuditService(AuditLogRepository repo) { this.repo = repo; }

    // always runs in its own transaction, so the log survives even if the caller rolls back
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String message) {
        repo.save(new AuditLog(message));
    }
}