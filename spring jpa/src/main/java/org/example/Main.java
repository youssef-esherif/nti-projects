package org.example;

import org.example.config.JpaConfig;
import org.example.dto.CategoryRevenue;
import org.example.dto.CustomerSpend;
import org.example.dto.MonthlySales;
import org.example.dto.OrderSummary;
import org.example.exception.DuplicateCustomerException;
import org.example.exception.InsufficientStockException;
import org.example.exception.InvalidOrderStateException;
import org.example.model.*;
import org.example.repository.ProductRepository;
import org.example.repository.ReportRepository;
import org.example.service.CustomerService;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(JpaConfig.class);

        CustomerService customerService = ctx.getBean(CustomerService.class);
        ProductService productService   = ctx.getBean(ProductService.class);
        OrderService orderService       = ctx.getBean(OrderService.class);
        ProductRepository productRepo   = ctx.getBean(ProductRepository.class);
        ReportRepository reportRepo     = ctx.getBean(ReportRepository.class);

        // 1. register customers
        Customer ali = customerService.register("Ali", "ali@mail.com",
                new Address("1 Nile St", "Cairo", "EG"));
        Customer mona = customerService.register("Mona", "mona@mail.com",
                new Address("2 Tahrir St", "Cairo", "EG"));

        // 2. duplicate email
        try {
            customerService.register("Ali2", "ali@mail.com", new Address("x", "y", "z"));
        } catch (DuplicateCustomerException e) {
            System.out.println(">> " + e.getMessage());
        }

        // 3. products
        Product laptop = productService.addProduct("SKU-1", "Laptop", new BigDecimal("15000"), 5);
        Product phone  = productService.addProduct("SKU-2", "Phone",  new BigDecimal("8000"), 10);

        // 4. place order
        Order order = orderService.placeOrder(ali.getId(),
                Map.of(laptop.getId(), 1, phone.getId(), 2));
        System.out.println(">> Order created: id=" + order.getId() + ", total=" + order.getTotal());

        // 5. insufficient stock
        try {
            orderService.placeOrder(mona.getId(), Map.of(laptop.getId(), 100));
        } catch (InsufficientStockException e) {
            System.out.println(">> " + e.getMessage());
        }

        // 6. invalid state (ship before pay)
        try {
            orderService.ship(order.getId());
        } catch (InvalidOrderStateException e) {
            System.out.println(">> " + e.getMessage());
        }

        // 7. pay + ship
        orderService.pay(order.getId(), PaymentMethod.CARD);
        orderService.ship(order.getId());
        System.out.println(">> Order " + order.getId() + " paid and shipped.");

        // 8. order summary
        OrderSummary summary = orderService.getOrderSummary(order.getId());
        System.out.println(">> Summary: " + summary.customerName() + " - " + summary.total());
        summary.lines().forEach(l -> System.out.println("   - " + l.product() + " x" + l.quantity()));

        // 9. reports
        System.out.println("\n=== revenue by category ===");
        for (CategoryRevenue r : reportRepo.revenueByCategory())
            System.out.println("   " + r);

        System.out.println("\n=== top customers ===");
        for (CustomerSpend s : reportRepo.topCustomers(5))
            System.out.println("   " + s);

        System.out.println("\n=== orders per status ===");
        reportRepo.ordersPerStatus().forEach((k, v) -> System.out.println("   " + k + " -> " + v));

        System.out.println("\n=== products never ordered ===");
        reportRepo.productsNeverOrdered().forEach(p -> System.out.println("   " + p));

        System.out.println("\n=== monthly sales (current year) ===");
        for (MonthlySales m : reportRepo.monthlySales(java.time.LocalDate.now().getYear()))
            System.out.println("   month " + m.month() + " -> " + m.total());

        // 10. bulk discount
        System.out.println("\n=== apply 10% discount ===");
        int updated = reportRepo.applyDiscount("Electronics", 10.0);
        System.out.println(">> updated rows: " + updated);

        ctx.close();
    }
}