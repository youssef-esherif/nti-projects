package com.example.annotations;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AroundAdviceAspect {
    @Around("execution(* com.example.annotations.ProductService.*(..))")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("BEFORE: " + jp.getSignature().getName()
                + " args=" + Arrays.toString(jp.getArgs()));
        try {
            Object result = jp.proceed();
            System.out.println("SUCCESS: " + result);
            return result;
        } catch (Throwable ex) {
            System.out.println("ERROR: " + ex.getMessage());
            throw ex;
        } finally {
            System.out.println("AFTER: always runs");
        }
    }
}
