package com.example.tasktracker.task;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TaskStore {
    private final Map<Long, Task> store = new HashMap<>();
    private final AtomicLong idGen = new AtomicLong();

    public Task add(Task task) {
        Long id = idGen.incrementAndGet();
        task.setId(id);
        store.put(id, task);
        return task;
    }

    public List<Task> findAll() {
        return new ArrayList<Task>(store.values());
    }

    //todo
    public List<Task> findByCompleted(boolean completed) {
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : store.values()) {
            if (task.isCompleted() == completed) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }

    public Task findById(Long id) {
        return store.get(id);
    }

    public Task update(Long id, Task task) {
        if (store.containsKey(id)) {
            task.setId(id);
            store.put(id, task);
            return task;
        }
        return null;
    }

    public Task markCompleted(Long id) {
        Task task = store.get(id);
        if (task == null)  return null;
        task.setCompleted(true);
        return task;
    }

    public boolean delete(Long id) {
        return store.remove(id) != null;
    }

    public int size() {
        return store.size();
    }

}
