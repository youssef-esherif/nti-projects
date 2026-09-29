package org.example.service;

import org.example.exception.DuplicateCustomerException;
import org.example.model.Address;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) { this.repo = repo; }

    @Transactional
    public Customer register(String name, String email, Address address) {
        if (repo.findByEmail(email).isPresent())
            throw new DuplicateCustomerException("Email already registered: " + email);
        return repo.save(new Customer(name, email, address));
    }
}