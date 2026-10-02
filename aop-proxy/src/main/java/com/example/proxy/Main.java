package com.example.proxy;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        NotificationService target = new NotificationServiceImpl();
        NotificationService proxy = (NotificationService) Proxy.newProxyInstance(
                NotificationService.class.getClassLoader(),
                new Class<?>[]{NotificationService.class},
                new LoggingHandler(target));

        // Call the proxy so logging wraps each real method.
        proxy.sendEmail("student@example.com", "Hello from Java!");
        System.out.println();
        proxy.sendSms("01000000000", "Your code is 1234");
    }
}
