package org.example.repository;

import org.example.model.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("prod")
public class FileBackedEmployeeRepository implements EmployeeRepository {

    private final File file = new File("employees.csv");
    private final List<Employee> cache = new ArrayList<>();

    @PostConstruct
    public void load() {
        System.out.println("[FileRepository] @PostConstruct - loading from " + file.getAbsolutePath());
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4) {
                    cache.add(new Employee(Integer.parseInt(p[0]), p[1], p[2], Double.parseDouble(p[3])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void save() {
        System.out.println("[FileRepository] @PreDestroy - writing to " + file.getAbsolutePath());
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (Employee e : cache) {
                pw.println(e.getId() + "," + e.getName() + "," + e.getDepartment() + "," + e.getSalary());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Employee e) {
        cache.removeIf(x -> x.getId() == e.getId());
        cache.add(e);
    }

    @Override
    public Employee findById(int id) {
        return cache.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(cache);
    }
}