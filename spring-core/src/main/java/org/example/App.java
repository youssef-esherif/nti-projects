package org.example;

import org.example.audit.AuditLogger;
import org.example.config.AppConfig;
import org.example.model.Employee;
import org.example.service.EmployeeService;
import org.example.service.EmployeeServiceImpl;
import org.example.service.InvalidEmployeeException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class);

        EmployeeService service = ctx.getBean(EmployeeService.class);

        System.out.println("\n=== Add valid employee ===");
        service.addEmployee(new Employee(1, "Ali", "IT", 5000));

        System.out.println("\n=== Try invalid employee (blank name) ===");
        try {
            service.addEmployee(new Employee(2, "", "HR", 4000));
        } catch (InvalidEmployeeException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Try invalid employee (negative salary) ===");
        try {
            service.addEmployee(new Employee(3, "Mona", "HR", -100));
        } catch (InvalidEmployeeException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Raise within limit ===");
        service.giveRaise(1, 10);

        System.out.println("\n=== Raise over limit ===");
        try {
            service.giveRaise(1, 50);
        } catch (InvalidEmployeeException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Prototype AuditLogger instances ===");
        AuditLogger a1 = ctx.getBean(AuditLogger.class);
        AuditLogger a2 = ctx.getBean(AuditLogger.class);
        AuditLogger a3 = ctx.getBean(AuditLogger.class);
        System.out.println("Different instances? " + (a1 != a2 && a2 != a3));

        System.out.println("\n=== All employees ===");
        service.getAllEmployees().forEach(System.out::println);

        System.out.println("\n=== Properties from @Value ===");
        System.out.println("company.name = " + ctx.getEnvironment().getProperty("company.name"));
        System.out.println("company.currency = " + ctx.getEnvironment().getProperty("company.currency"));
        System.out.println("raise.max-percentage = " + ctx.getEnvironment().getProperty("raise.max-percentage"));
        System.out.println("notification.retry-count = " + ctx.getEnvironment().getProperty("notification.retry-count"));

        System.out.println("\n=== Closing context (triggers @PreDestroy) ===");
        ctx.close();
    }
}
