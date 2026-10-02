<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Tasks</title></head>
<body>
<h1>Tasks</h1>
<p>
<a href="${pageContext.request.contextPath}/tasks/new">Create a task</a> |
<a href="${pageContext.request.contextPath}/tasks">All tasks</a> |
<a href="${pageContext.request.contextPath}/tasks/search?priority=HIGH">High priority</a> |
<a href="${pageContext.request.contextPath}/tasks/search?priority=MEDIUM">Medium priority</a> |
<a href="${pageContext.request.contextPath}/tasks/search?priority=LOW">Low priority</a>
</p>
<c:if test="${not empty selectedPriority}"><p>Priority: <c:out value="${selectedPriority}"/></p></c:if>
<c:if test="${empty tasks}"><p>No tasks found. Create a task to get started.</p></c:if>
<%-- Plain JSTL reads the "tasks" attribute that the controller added to Model. --%>
<ul>
<c:forEach var="task" items="${tasks}">
    <li><a href="${pageContext.request.contextPath}/tasks/${task.id}"><c:out value="${task.title}"/></a>
    - <c:out value="${task.priority}"/> - Completed: <c:out value="${task.completed}"/></li>
</c:forEach>
</ul>
</body>
</html>
