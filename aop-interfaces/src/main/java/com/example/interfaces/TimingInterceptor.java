package com.example.interfaces;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TimingInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.nanoTime();
        System.out.println("AROUND: start " + invocation.getMethod().getName());
        try {
            return invocation.proceed();
        } finally {
            System.out.println("FINALLY: " + invocation.getMethod().getName() + " took "
                    + (System.nanoTime() - start) / 1_000_000.0 + " ms");
        }
    }
}
