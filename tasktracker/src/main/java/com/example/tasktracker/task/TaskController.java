package com.example.tasktracker.task;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskStore store;
    @Value("${tasktracker.max-tasks}")
    private int maxTasks;

    @Value("${tasktracker.default-page-size}")
    private int defaultPageSize;

    public TaskController(TaskStore store) {
        this.store = store;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Integer limit) {
        List<Task> tasks = (completed == null) ? store.findAll() : store.findByCompleted(completed);

        int size = (limit == null) ? defaultPageSize : limit;

        if (tasks.size() > size) tasks = tasks.subList(0, size);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        if (store.size() >= maxTasks) return new ResponseEntity<>(HttpStatus.CONFLICT);
        Task newTask = store.add(task);
        URI location = URI.create("/api/tasks/" + task.getId());
        return ResponseEntity.created(location).body(newTask);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = store.findById(id);
        if (task == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task taskToUpdate = store.update(id, task);
        if (taskToUpdate == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(taskToUpdate);
    }

    @PatchMapping("/tasks/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) {
        Task task = store.markCompleted(id);
        if (task == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean deleteTask= store.delete(id);
        if (deleteTask) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
