<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String errore = (String) request.getAttribute("errore"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Zampastico</title>
 	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
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
    <script src="${pageContext.request.contextPath}/scripts/main.js"></script>
</body>
</html>