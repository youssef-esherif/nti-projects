# Spring MVC task practice

## Run
Rebuild and restart your existing Tomcat configuration, then open /MVC2/tasks on that server.

Alternatively, run from this folder:
    mvn package cargo:run
Then open http://localhost:8082/session2-lab-part1/tasks.
This needs Java 17+, Maven, and internet access for the first dependency download.

## Read in this order
1. Task.java: form fields, getters/setters, @NotBlank, @NotNull.
2. Priority.java: LOW, MEDIUM, HIGH.
3. TaskRepository.java: ArrayList storage, IDs, and filtering loops.
4. TaskController.java: mappings, Model, binding, validation, redirect.
5. WEB-INF/views: JSTL read pages and Spring form tags.
6. TaskNotFoundException.java and GlobalExceptionHandler.java: custom 404.
7. AppConfig.java and WebConfig.java: Spring MVC setup and JSP view resolution.

## Practice
All paths below are relative to your application's context path.
- GET /tasks: list tasks using plain JSTL, no forms.
- GET /tasks/new: show the creation form.
- POST /tasks: bind fields, validate, save, redirect to the list.
- Submit a blank title and no priority to see both validation messages.
- Create one HIGH task and one LOW task.
- GET /tasks/1: read the first task using @PathVariable.
- GET /tasks/search?priority=LOW: filter using @RequestParam.
- GET /tasks/search: default priority is HIGH.
- GET /tasks/999: custom error JSP with HTTP status 404.

## Remember
BindingResult must immediately follow the @Valid object.
Returning the form view on errors preserves values and validation messages.
@InitBinder allows only title, priority, and completed; the repository assigns IDs.
The advice method sets @ResponseStatus too, because it handles the exception while returning a view.
The updated Servlet 6 web.xml enables JSP expression evaluation.
Tasks are stored only in memory and disappear on restart. This simple repository is for learning.
