<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="../common-head.jsp" %>
    <title>Registrati - Zampastico</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <jsp:include page="../header.jsp" />

    <main class="auth-container container">
        <div class="auth-card">
            
            <div class="auth-header">
                <h1>Unisciti al branco! <i class="fa-solid fa-dog" style="color: var(--color-primary, #f37925);"></i></h1>
                <p>Crea il tuo account Zampastico per salvare i tuoi ordini e i prodotti preferiti.</p>
            </div>

            <form id="regForm" action="${pageContext.request.contextPath}/registrazione" method="POST">
                
                <div class="form-group">
                    <label for="nome">Nome *</label>
                    <input type="text" id="nome" name="nome" class="form-control">
                    <span id="err-nome" class="error-msg"></span>
                </div>

                <div class="form-group">
                    <label for="cognome">Cognome *</label>
                    <input type="text" id="cognome" name="cognome" class="form-control">
                    <span id="err-cognome" class="error-msg"></span>
                </div>

                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" id="email" name="email" class="form-control">
                    <span id="err-email" class="error-msg"></span>
                    
                    <%-- Errore lato Server (es. Email già esistente) --%>
                    <% if(request.getAttribute("erroreEmail") != null) { %>
                        <span class="error-msg server-error"><i class="fa-solid fa-triangle-exclamation"></i> <%= request.getAttribute("erroreEmail") %></span>
                    <% } %>
                </div>

                <div class="form-group">
                    <label for="password">Password *</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="Min. 8 caratteri, una lettera e un numero">
                    <span id="err-password" class="error-msg"></span>
                </div>

                <button type="submit" class="btn-primary btn-block">Registrati</button>
                
                <div class="auth-footer">
                    <p>Hai già un account? <a href="${pageContext.request.contextPath}/login" class="auth-link">Accedi qui</a></p>
                </div>
            </form>
        </div>
    </main>

    <jsp:include page="../footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/scripts/main.js?v=3"></script>
</body>
</html>