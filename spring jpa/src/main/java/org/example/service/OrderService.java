package org.example.service;

import org.example.dto.OrderSummary;
import org.example.exception.InsufficientStockException;
import org.example.exception.InvalidOrderStateException;
import org.example.exception.NotFoundException;
import org.example.model.*;
import org.example.repository.CustomerRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final CustomerRepository customerRepo;
    private final AuditService auditService;

    public OrderService(OrderRepository orderRepo,
                        ProductRepository productRepo,
                        CustomerRepository customerRepo,
                        AuditService auditService) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
        this.auditService = auditService;
    }

    @Transactional
    public Order placeOrder(Long customerId, Map<Long, Integer> productQty) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + customerId));

        Order order = new Order();
        order.setCustomer(customer);

        for (Map.Entry<Long, Integer> e : productQty.entrySet()) {
            int qty = e.getValue();
            if (qty <= 0)
                throw new InsufficientStockException("Quantity must be > 0 for product " + e.getKey());

            Product p = productRepo.findById(e.getKey())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + e.getKey()));

            if (p.getStock() < qty)
                throw new InsufficientStockException(
                        "Insufficient stock for " + p.getSku() + ": have " + p.getStock() + ", need " + qty);

            p.setStock(p.getStock() - qty);
            order.addItem(new OrderItem(p, qty, p.getPrice()));
        }

        Order saved = orderRepo.save(order);
        auditService.log("Order placed: " + saved.getId());
        return saved;
    }

    @Transactional
    public void pay(Long orderId, PaymentMethod method) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        if (o.getStatus() != OrderStatus.NEW)
            throw new InvalidOrderStateException("Only NEW orders can be paid, status is " + o.getStatus());

        Payment payment = new Payment(o, o.getTotal(), method);
        o.setPayment(payment);
        o.setStatus(OrderStatus.PAID);
        // dirty checking saves the status change
        auditService.log("Order paid: " + orderId);
    }

    @Transactional
    public void ship(Long orderId) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        if (o.getStatus() != OrderStatus.PAID)
            throw new InvalidOrderStateException("Only PAID orders can be shipped, status is " + o.getStatus());
        o.setStatus(OrderStatus.SHIPPED);
    }

    @Transactional
    public void cancel(Long orderId) {
        Order o = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        if (o.getStatus() != OrderStatus.NEW && o.getStatus() != OrderStatus.PAID)
            throw new InvalidOrderStateException("Cannot cancel order in status " + o.getStatus());

        for (OrderItem item : o.getItems()) {
            Product p = item.getProduct();
            p.setStock(p.getStock() + item.getQuantity());
        }
        o.setStatus(OrderStatus.CANCELLED);
    }

    @Transactional(readOnly = true)
    public OrderSummary getOrderSummary(Long orderId) {
        Order o = orderRepo.findByIdWithItems(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        List<OrderSummary.Line> lines = new ArrayList<>();
        for (OrderItem i : o.getItems())
            lines.add(new OrderSummary.Line(i.getProduct().getName(), i.getQuantity(), i.getUnitPrice()));

        return new OrderSummary(
                o.getId(),
                o.getCustomer().getName(),
                o.getStatus().name(),
                o.getTotal(),
                lines);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotal(Long orderId) {
        Order o = orderRepo.findByIdWithItems(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));
        return o.getTotal();
    }
}