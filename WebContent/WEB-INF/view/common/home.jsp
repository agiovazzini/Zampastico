<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.UtenteBEAN" %>
<% UtenteBEAN utente = (UtenteBEAN) session.getAttribute("utenteLoggato"); %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common-head.jsp" %>
    <title>Zampastico - Home</title>
</head>
<body>
    <% String infoMsg = (String) request.getAttribute("statoAccesso"); %>
    <% if (infoMsg != null) { %>
        <div>
            <%= infoMsg %>
        </div>
    <% } %>
    <% if (utente != null) { %>
        <div>
            <p>Ciao, <%= utente.getNome() %> <%= utente.getCognome() %>!</p>
            <p>La tua email: <%= utente.getEmail() %></p>
            <a href="${pageContext.request.contextPath}/logout">Esci dall'account</a>
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