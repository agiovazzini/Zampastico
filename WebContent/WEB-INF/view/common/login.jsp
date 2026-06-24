<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String errore = (String) request.getAttribute("errore"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Accedi - Zampastico</title>
    <%@ include file="common-head.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>

    <%@ include file="header.jsp" %>

    <main class="login-container container">
        <div class="login-card">
            
            <div class="login-header">
                <h2>Bentornato! <i class="fa-solid fa-paw"></i></h2>
                <p>Accedi per continuare lo shopping.</p>
            </div>

            <% if (errore != null) { %>
                <div class="error-message">
                    <i class="fa-solid fa-circle-exclamation"></i> <%= errore %>
                </div>
            <% } %>

            <form id="loginForm" action="${pageContext.request.contextPath}/login" method="POST" class="login-form">
                <div class="form-group">
                    <label for="username">Email:</label>
                    <input type="email" id="username" name="username" placeholder="Inserisci la tua email" required>
                </div>
                
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" placeholder="Inserisci la tua password" required>
                </div>

                <button type="submit" class="btn-primary btn-block">Accedi</button>
            </form>

            <div class="login-footer">
                <p>Non hai un account? <a href="${pageContext.request.contextPath}/register.jsp" class="register-link">Registrati ora</a></p>
            </div>
        </div>
    </main>

    <%@ include file="footer.jsp" %>
    <script src="${pageContext.request.contextPath}/scripts/main.js"></script>
</body>
</html>