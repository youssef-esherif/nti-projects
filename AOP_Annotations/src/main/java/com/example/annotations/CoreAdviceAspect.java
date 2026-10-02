package com.example.annotations;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class CoreAdviceAspect {
    @Pointcut("execution(* com.example.annotations.ProductService.*(..))")
    public void productMethods() {}

    @Before("productMethods()")
    public void before(JoinPoint jp) {
        System.out.println("BEFORE: " + jp.getSignature().getName()
                + " args=" + Arrays.toString(jp.getArgs()));
    }

    @AfterReturning(pointcut = "productMethods()", returning = "result")
    public void success(Object result) {
        System.out.println("SUCCESS: " + result);
    }

    @AfterThrowing(pointcut = "productMethods()", throwing = "ex")
    public void error(Exception ex) {
        System.out.println("ERROR: " + ex.getMessage());
    }

    @After("productMethods()")
    public void after() {
        System.out.println("AFTER: always runs");
    }
}
