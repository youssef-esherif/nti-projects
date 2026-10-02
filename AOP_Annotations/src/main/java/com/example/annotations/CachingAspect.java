package com.example.annotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class CachingAspect {
    private final Map<String, Object> cache = new HashMap<>();

    @Around("@annotation(com.example.annotations.Cacheable)")
    public Object cache(ProceedingJoinPoint jp) throws Throwable {
        String key = jp.getSignature().toLongString() + Arrays.toString(jp.getArgs());
        if (cache.containsKey(key)) {
            System.out.println("CACHE HIT: " + Arrays.toString(jp.getArgs()));
            return cache.get(key);
        }
        System.out.println("CACHE MISS: " + Arrays.toString(jp.getArgs()));
        Object result = jp.proceed();
        cache.put(key, result);
        return result;
    }
}
