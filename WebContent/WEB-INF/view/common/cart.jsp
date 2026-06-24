<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common-head.jsp" %>
<title>Il tuo carrello | Zampastico</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">

<!-- FontAwesome per l'icona del cestino -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<jsp:include page="header.jsp"/>

<main class="cart-container container">
    <h1 class="cart-title">Il tuo carrello <i class="fa-solid fa-paw"></i></h1>

    <div class="cart-layout">
        <div class="cart-items-section">
            
            <div class="empty-cart-message">
                <i class="fa-solid fa-basket-shopping empty-icon"></i>
                <h2>Il tuo carrello è vuoto! 🐾</h2>
                <p>Non hai ancora scelto nessun prodotto per il tuo amico a 4 zampe.</p>
                <a href="${pageContext.request.contextPath}/home" class="btn-primary">Inizia lo shopping</a>
            </div>

            </div>

        <div class="cart-summary-section">
            <h3 class="summary-title">Riepilogo Ordine</h3>
            
            <div class="summary-row">
                <span id="summary-count">Subtotale (0 articoli)</span>
                <span id="summary-subtotal">0,00€</span>
            </div>
            
            <div class="summary-row">
                <span>Spedizione</span>
                <span class="free-shipping">-</span>
            </div>
            
            <hr class="summary-divider">
            
            <div class="summary-row total-row">
                <span>Totale:</span>
                <span id="summary-total">0,00€</span>
            </div>
            
            <a href="#" class="btn-primary btn-block" style="opacity: 0.5; pointer-events: none;">Procedi all'ordine</a>
            
            <div class="secure-checkout">
                <i class="fa-solid fa-shield-halved"></i> Pagamento Sicuro
            </div>
        </div>
    </div>
</main>

<script>
document.addEventListener('DOMContentLoaded', function() {
    console.log("Il JavaScript del carrello è partito!"); // Premi F12 sul browser per vedere questo messaggio!

    function updateTotals() {
        let total = 0;
        let itemCount = 0;
        
        const cards = document.querySelectorAll('.cart-item-card');
        if(cards.length === 0) return; // Se il carrello è vuoto, non fare nulla

        cards.forEach(card => {
            // Recupera il prezzo. Se non c'è il data-base-price, usa 0 per non far crashare il sito
            const priceAttr = card.getAttribute('data-base-price');
            const price = priceAttr ? parseFloat(priceAttr) : 0;
            
            const qtyInput = card.querySelector('.qty-input');
            const qty = qtyInput ? parseInt(qtyInput.value) : 1;
            
            const lineTotal = price * qty;
            
            const priceDisplay = card.querySelector('.item-price');
            if(priceDisplay) priceDisplay.textContent = lineTotal.toFixed(2).replace('.', ',') + '€';
            
            total += lineTotal;
            itemCount += qty;
        });
        
        const countDisplay = document.getElementById('summary-count');
        const subtotalDisplay = document.getElementById('summary-subtotal');
        const totalDisplay = document.getElementById('summary-total');

        if(countDisplay) countDisplay.textContent = 'Subtotale (' + itemCount + ' articoli)';
        if(subtotalDisplay) subtotalDisplay.textContent = total.toFixed(2).replace('.', ',') + '€';
        if(totalDisplay) totalDisplay.textContent = total.toFixed(2).replace('.', ',') + '€';
    }

    // Aggiungiamo i click ai bottoni
    document.querySelectorAll('.cart-item-card').forEach(card => {
        const btnMinus = card.querySelector('.btn-minus');
        const btnPlus = card.querySelector('.btn-plus');
        const input = card.querySelector('.qty-input');
        const btnRemove = card.querySelector('.remove-btn');
        
        if(btnPlus && input) {
            btnPlus.addEventListener('click', function() {
                input.value = parseInt(input.value) + 1;
                updateTotals(); 
            });
        }
        
        if(btnMinus && input) {
            btnMinus.addEventListener('click', function() {
                if (parseInt(input.value) > 1) {
                    input.value = parseInt(input.value) - 1;
                    updateTotals(); 
                }
            });
        }

        if(btnRemove) {
            btnRemove.addEventListener('click', function() {
                card.remove(); 
                updateTotals(); 
            });
        }
    });
    
    // Calcolo iniziale
    updateTotals();
});
</script>

<!-- Inclusione nel footer -->
<jsp:include page="footer.jsp" />

<!-- Inclusione dello script JS -->
<script src="${pageContext.request.contextPath}/scripts/main.js"></script>
</body>
</html>