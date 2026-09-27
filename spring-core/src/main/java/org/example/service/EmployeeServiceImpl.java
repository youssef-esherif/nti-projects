package org.example.service;

import org.example.audit.AuditLogger;
import org.example.model.Employee;
import org.example.notify.NotificationManager;
import org.example.repository.EmployeeRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeValidator validator;
    private final NotificationManager notificationManager;
    private final ObjectProvider<AuditLogger> auditLoggerProvider;

    @Value("${company.name}")
    private String companyName;

    @Value("${raise.max-percentage}")
    private double raiseMaxPercentage;

    // Constructor injection for required deps
    public EmployeeServiceImpl(EmployeeRepository repository,
                               EmployeeValidator validator,
                               NotificationManager notificationManager,
                               ObjectProvider<AuditLogger> auditLoggerProvider) {
        this.repository = repository;
        this.validator = validator;
        this.notificationManager = notificationManager;
        this.auditLoggerProvider = auditLoggerProvider;
    }

    @Override
    public void addEmployee(Employee e) {
        validator.validate(e);
        repository.save(e);
        auditLoggerProvider.getObject().log("Added employee " + e.getId());
        notificationManager.notifyAll("New employee added: " + e.getName());
    }

    @Override
    public Employee getEmployeeById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public void giveRaise(int id, double percentage) {
        Employee e = repository.findById(id);
        if (e == null) throw new InvalidEmployeeException("Employee not found: " + id);
        if (percentage > raiseMaxPercentage)
            throw new InvalidEmployeeException("Raise " + percentage + "% exceeds max " + raiseMaxPercentage + "%");

        double newSalary = e.getSalary() * (1 + percentage / 100.0);
        e.setSalary(newSalary);
        repository.save(e);

        auditLoggerProvider.getObject().log("Raise for " + id + " by " + percentage + "%");
        notificationManager.notifyAll("Raise given to " + e.getName() + " (" + percentage + "%)");
    }

    public String getCompanyName() { return companyName; }
}