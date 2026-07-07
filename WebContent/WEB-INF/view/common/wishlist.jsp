<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>I miei Articoli Preferiti - Zampastico</title>
    <%@ include file="common-head.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>

    <!-- Richiama l'header con il menu -->
    <%@ include file="header.jsp" %>

    <main class="wishlist-container container">
        <div class="cart-title">
            I miei Preferiti <i class="fa-solid fa-heart text-orange"> :</i>
        </div>

        <!-- Griglia dove JavaScript inserirà i prodotti salvati -->
        <div id="wishlist-content" class="wishlist-grid">
    	</div>

        <!-- Messaggio che appare solo se la wishlist è vuota -->
        <div id="empty-wishlist" class="empty-cart-message" style="display: none;">
            <i class="fa-solid fa-heart-crack empty-icon"></i>
            <h2>Nessun preferito!</h2>
            <p>Non hai ancora aggiunto prodotti alla tua Lista dei Desideri.</p>
            <a href="${pageContext.request.contextPath}/home" class="btn-primary">Torna allo shopping</a>
        </div>
    </main>

    <!-- Richiama il footer e il Javascript -->
    <%@ include file="footer.jsp" %>
    <script src="${pageContext.request.contextPath}/scripts/main.js"></script>
    <script>
        // Appena la pagina è pronta, carica i preferiti salvati!
        document.addEventListener('DOMContentLoaded', window.caricaWishlist);
    </script>
</body>
</html>