<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><title>Create task</title><style>.error { color: #b00020; }</style></head>
<body>
<h1>Create task</h1>
<%-- Each path names a Task property. Spring uses its getters and setters. --%>
<form:form modelAttribute="task" method="post" action="${pageContext.request.contextPath}/tasks" htmlEscape="true">
<p>
    <form:label path="title">Title</form:label>
    <form:input path="title"/>
    <form:errors path="title" cssClass="error"/>
</p>
<p>
    <form:label path="priority">Priority</form:label>
    <form:select path="priority">
        <form:option value="" label="Choose a priority"/>
        <form:option value="LOW" label="Low"/>
        <form:option value="MEDIUM" label="Medium"/>
        <form:option value="HIGH" label="High"/>
    </form:select>
    <form:errors path="priority" cssClass="error"/>
</p>
<p><form:checkbox path="completed"/> <form:label path="completed">Completed</form:label></p>
<button type="submit">Save task</button>
</form:form>
<p><a href="${pageContext.request.contextPath}/tasks">Back to tasks</a></p>
</body>
</html>
