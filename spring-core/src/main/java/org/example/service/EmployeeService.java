package org.example.service;

import org.example.model.Employee;
import java.util.List;

public interface EmployeeService {
    void addEmployee(Employee e);
    Employee getEmployeeById(int id);
    List<Employee> getAllEmployees();
    void giveRaise(int id, double percentage);
}