package org.example.service;

import org.example.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator {

    public void validate(Employee e) {
        if (e == null) throw new InvalidEmployeeException("Employee is null");
        if (e.getName() == null || e.getName().isBlank())
            throw new InvalidEmployeeException("Name is blank");
        if (e.getSalary() < 0)
            throw new InvalidEmployeeException("Salary is negative: " + e.getSalary());
    }
}