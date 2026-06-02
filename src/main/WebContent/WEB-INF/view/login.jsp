<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String errore = (String) request.getAttribute("errore"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Zampastico</title>
</head>
<body>
	<% if (errore != null) { %>
        <div>
            <%= errore %>
        </div>
    <% } %>
    <form id="loginForm" action="${pageContext.request.contextPath}/login" method="POST">
    <label for="username">Email:</label><br>
    <input type="email" id="username" name="username" required><br>
    <label for="password">Password:</label><br>
    <input type="password" id="password" name="password" required><br><br>
    <input type="submit" value="Accedi">
</form>
</body>
</html>