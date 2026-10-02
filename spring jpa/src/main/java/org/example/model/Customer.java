package org.example.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Embedded
    private Address shippingAddress;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();

    public Customer() {}
    public Customer(String name, String email, Address shippingAddress) {
        this.name = name; this.email = email; this.shippingAddress = shippingAddress;
    }

    public void addOrder(Order o) { orders.add(o); o.setCustomer(this); }
    public void removeOrder(Order o) { orders.remove(o); o.setCustomer(null); }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Address a) { this.shippingAddress = a; }
    public Set<Order> getOrders() { return orders; }
}