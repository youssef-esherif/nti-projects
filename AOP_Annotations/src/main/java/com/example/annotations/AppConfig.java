package com.example.annotations;

import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
    @Bean
    public ProductService productService() { return new ProductService(); }

    @Bean
    @Profile("core")
    public CoreAdviceAspect coreAdviceAspect() { return new CoreAdviceAspect(); }

    @Bean
    @Profile("around")
    public AroundAdviceAspect aroundAdviceAspect() { return new AroundAdviceAspect(); }

    @Bean
    @Profile("cache")
    public CachingAspect cachingAspect() { return new CachingAspect(); }
}
