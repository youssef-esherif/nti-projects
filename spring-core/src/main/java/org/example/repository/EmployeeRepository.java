package org.example.repository;

import org.example.model.Employee;
import java.util.List;

public interface EmployeeRepository {
    void save(Employee e);
    Employee findById(int id);
    List<Employee> findAll();
}