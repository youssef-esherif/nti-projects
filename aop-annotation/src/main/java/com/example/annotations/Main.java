package com.example.annotations;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        runDemo("core");
        runDemo("around");
        runDemo("cache");
    }

    private static void runDemo(String profile) {
        System.out.println("\n=== " + profile + " demo ===");
        try (var context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles(profile);
            context.register(AppConfig.class);
            context.refresh();
            ProductService service = context.getBean(ProductService.class);

            System.out.println("RESULT: " + service.getProduct("1"));
            if (profile.equals("cache")) {
                System.out.println("RESULT: " + service.getProduct("1"));
                System.out.println("RESULT: " + service.getProduct("2"));
            }
            callMissingProduct(service);
            if (profile.equals("cache")) {
                callMissingProduct(service);
            }
        }
    }

    private static void callMissingProduct(ProductService service) {
        try {
            service.getProduct("bad");
        } catch (IllegalArgumentException ex) {
            System.out.println("CALLER caught: " + ex.getMessage());
        }
    }
}
