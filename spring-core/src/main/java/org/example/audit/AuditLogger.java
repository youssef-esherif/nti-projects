package org.example.audit;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;

@Component
@Scope("prototype")   // new instance each time it's requested
public class AuditLogger {

    private final int instanceId;

    public AuditLogger() {
        this.instanceId = System.identityHashCode(this);
        System.out.println("[AuditLogger] new instance created -> " + instanceId);
    }

    @PostConstruct
    public void init() {
        System.out.println("[AuditLogger] @PostConstruct on " + instanceId);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[AuditLogger] @PreDestroy on " + instanceId);
    }

    public void log(String msg) {
        System.out.println("[AUDIT " + LocalDateTime.now() + " | id=" + instanceId + "] " + msg);
    }
}