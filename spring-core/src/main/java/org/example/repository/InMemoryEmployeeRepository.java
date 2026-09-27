package org.example.repository;

import org.example.model.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("dev")
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final List<Employee> store = new ArrayList<>();

    @PostConstruct
    public void init() {
        System.out.println("[InMemoryRepository] @PostConstruct - initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("[InMemoryRepository] @PreDestroy - destroyed");
    }

    @Override
    public void save(Employee e) {
        store.removeIf(x -> x.getId() == e.getId());
        store.add(e);
    }

    @Override
    public Employee findById(int id) {
        return store.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(store);
    }
}