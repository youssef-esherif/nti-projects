package org.example.library.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("EMPLOYEE")
public class Employee extends Person {

    @Column(name = "salary")
    private Double salary;

    public Employee() {}

    public Employee(String name, Double salary) {
        super(name);
        this.salary = salary;
    }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
}