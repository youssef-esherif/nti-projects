<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Task details</title></head>
<body>
<h1><c:out value="${task.title}"/></h1>
<p>ID: <c:out value="${task.id}"/></p>
<p>Priority: <c:out value="${task.priority}"/></p>
<p>Completed: <c:out value="${task.completed}"/></p>
<a href="${pageContext.request.contextPath}/tasks">Back to tasks</a>
</body>
</html>
