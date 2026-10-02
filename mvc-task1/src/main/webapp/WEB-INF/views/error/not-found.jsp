<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Task not found</title></head>
<body>
<h1>404 - Task not found</h1>
<p><c:out value="${message}"/></p>
<a href="${pageContext.request.contextPath}/tasks">Back to tasks</a>
</body>
</html>
