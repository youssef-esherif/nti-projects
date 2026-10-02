package com.example.interfaces;

import java.lang.reflect.Method;
import org.springframework.aop.AfterReturningAdvice;

public class LoggingAfterReturningAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object result, Method method, Object[] args, Object target) {
        if (method.getName().equals("checkStock")) {
            System.out.println("SUCCESS: checkStock returned " + result);
        }
    }
}
