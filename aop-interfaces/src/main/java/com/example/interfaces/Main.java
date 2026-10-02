package com.example.interfaces;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. Manual ProxyFactory ===");
        demonstrate(createProxy(false));

        System.out.println("\n=== 2. Spring-managed ProxyFactoryBean ===");
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            InventoryService service = context.getBean("inventoryService", InventoryService.class);
            demonstrate(service);
        }

        System.out.println("\n=== 3. Bonus: BEFORE only for reserveStock ===");
        demonstrate(createProxy(true));
    }

    private static InventoryService createProxy(boolean selectiveLogging) {
        ProxyFactory factory = new ProxyFactory(new InventoryServiceImpl());
        factory.addAdvice(new TimingInterceptor());
        if (selectiveLogging) {
            NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
            pointcut.setMappedName("reserveStock");
            factory.addAdvisor(new DefaultPointcutAdvisor(pointcut, new LoggingBeforeAdvice()));
        } else {
            factory.addAdvice(new LoggingBeforeAdvice());
        }
        factory.addAdvice(new LoggingAfterReturningAdvice());
        factory.addAdvice(new LoggingThrowsAdvice());
        return (InventoryService) factory.getProxy();
    }

    private static void demonstrate(InventoryService service) {
        service.checkStock("BOOK");
        service.reserveStock("BOOK", 10);
        try {
            service.reserveStock("BOOK", 150);
        } catch (IllegalStateException ex) {
            System.out.println("CALLER caught: " + ex.getMessage());
        }
    }
}
