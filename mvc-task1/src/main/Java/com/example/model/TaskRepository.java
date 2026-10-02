package com.example.model;
import com.example.exception.TaskNotFoundException;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {
    private final List<Task> taskList = new ArrayList<>();
    private int nextId = 1;

    public List<Task> getTasks() { return taskList; }

    public Task getTask(int id) {
        for (Task task : taskList) if (task.getId() == id) return task;
        throw new TaskNotFoundException(id);
    }

    public void addTask(Task task) {
        task.setId(nextId++);
        taskList.add(task);
    }

    public List<Task> findByPriority(Priority priority) {
        List<Task> result = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getPriority() == priority) result.add(task);
        }
        return result;
    }
}
