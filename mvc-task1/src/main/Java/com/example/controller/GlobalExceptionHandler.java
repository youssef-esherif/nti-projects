package com.example.controller;
import com.example.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(TaskNotFoundException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error/not-found";
    }
}