package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Version
    private Integer version;

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Integer getVersion() { return version; }
}