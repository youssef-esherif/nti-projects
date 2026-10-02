package com.example.interfaces;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public TimingInterceptor timingAdvice() { return new TimingInterceptor(); }

    @Bean
    public LoggingBeforeAdvice beforeAdvice() { return new LoggingBeforeAdvice(); }

    @Bean
    public LoggingAfterReturningAdvice successAdvice() { return new LoggingAfterReturningAdvice(); }

    @Bean
    public LoggingThrowsAdvice errorAdvice() { return new LoggingThrowsAdvice(); }

    @Bean
    public ProxyFactoryBean inventoryService() {
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.setTarget(new InventoryServiceImpl());
        factory.setInterfaces(InventoryService.class);
        // Timing is first, so its finally block runs after the other advice.
        factory.setInterceptorNames("timingAdvice", "beforeAdvice", "successAdvice", "errorAdvice");
        return factory;
    }
}
