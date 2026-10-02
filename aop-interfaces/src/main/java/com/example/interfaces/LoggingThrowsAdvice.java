package com.example.interfaces;

import java.lang.reflect.Method;
import org.springframework.aop.ThrowsAdvice;

public class LoggingThrowsAdvice implements ThrowsAdvice {
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) {
        if (method.getName().equals("reserveStock")) {
            System.out.println("ERROR: reserveStock - " + ex.getMessage());
        }
    }
}
