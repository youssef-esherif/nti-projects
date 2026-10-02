package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggingHandler implements InvocationHandler {
    private final Object target;

    public LoggingHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("BEFORE: " + method.getName() + " args=" + Arrays.toString(args));
        long start = System.nanoTime();
        try {
            Object result = method.invoke(target, args);
            System.out.println("RETURN: " + result);
            return result;
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        } finally {
            System.out.println("TIME: " + (System.nanoTime() - start) / 1_000_000.0 + " ms");
        }
    }
}
