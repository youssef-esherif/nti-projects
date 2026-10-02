package com.example.controller;

import com.example.model.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @InitBinder("task")
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("title", "priority", "completed");
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tasks", taskRepository.getTasks());
        return "task-list";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("task", taskRepository.getTask(id));
        return "task-detail";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "priority", defaultValue = "HIGH") Priority priority, Model model) {
        model.addAttribute("tasks", taskRepository.findByPriority(priority));
        model.addAttribute("selectedPriority", priority);
        return "task-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("task", new Task());
        return "task-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("task") Task task, BindingResult result) {
        if (result.hasErrors()) return "task-form";
        taskRepository.addTask(task);
        return "redirect:/tasks";
    }
}
