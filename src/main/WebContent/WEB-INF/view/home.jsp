<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.zampastico.model.UtenteBEAN" %>
<% UtenteBEAN utente = (UtenteBEAN) session.getAttribute("utenteLoggato"); %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Zampastico - Home</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
    <% if (utente != null) { %>
        <div>
            <p>Ciao, <%= utente.getNome() %> <%= utente.getCognome() %>!</p>
            <p>La tua email: <%= utente.getEmail() %></p>
            <br>
        </div>
    <% } else { %>
        
        <div>
            <h2>Non sei autenticato</h2>
            <br>
            <a href="${pageContext.request.contextPath}/login">Vai alla pagina di Login</a>
        </div>
        
    <% } %>
    <script src="${pageContext.request.contextPath}/scripts/main.js"></script>
</body>
</html>