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
			<div class="cart-item-card" data-base-price="50.99">
				<div class="cart-item-image">
					<img src="${pageContext.request.contextPath}/images/assets/monge_salmone.png" alt="Monge">
				</div>
				<div class="cart-item-details">
					<span class="item-category">Cani</span>
					<h3 class="item-name">Monge Natural Superpremium</h3>
					<p class="item-weight">Crocchette per cani di taglia piccola</p>
				</div>
				<div class="cart-item-actions">
					<div class="qty-control">
						<button class="qty-btn btn-minus">-</button>
						<input type="number" value="1" min="1" class="qty-input" readonly>
						<button class="qty-btn btn-plus">+</button>
					</div>
				</div>
				<div class="cart-item-price-wrap">
					<span class="item-price">50,99€</span>
					<button class="remove-btn" title="Rimuovi dal carrello"><i class="fa-solid fa-trash-can"></i></button>
				</div>
			</div>
			
			<!-- Secondo prodotto -->
			<div class="cart-item-card" data-base-price="8.40">
				<div class="cart-item-image">
					<img src="${pageContext.request.contextPath}/images/assets/palla_a_corda.jpg" alt="Palla">
				</div>
				<div class="cart-item-details">
					<span class="item-category">Giochi</span>
					<h3 class="item-name">Palla in corda</h3>
					<p class="item-weight">Gioco per cani.</p>
				</div>
				<div class="cart-item-actions">
					<div class="qty-control">
						<button class="qty-btn btn-minus">-</button>
						<input type="number" value="2" min="1" class="qty-input" readonly>
						<button class="qty-btn btn-plus">+</button>
					</div>
				</div>
				<div class="cart-item-price-wrap">
					<span class="item-price">16,80€</span>
					<button class="remove-btn" title="Rimuovi dal carrello"><i class="fa-solid fa-trash-can"></i></button>
				</div>
			</div>
		</div>
	
	<!-- Colonna destra: Riepilogo Ordine -->
	<div class="cart-summary-section">
		<h3 class="summary-title">Riepilogo Ordine</h3>

		<div class="summary-row">
			<span id="summary-count">Subtotale (0 articoli)</span>
			<span id="summary-subtotal">0,00€</span>
		</div>

		<div class="summary-row">
			<span>Spedizione</span>
			<span class="free-shipping">Gratuita</span>
		</div>
		
		<hr class="summary-divider">
		
		<div class="summary-row total-row">
			<span>Totale:</span>
			<span id="summary-total">0,00€</span>
		</div>
		
		<a href="${pageContext.request.contextPath}/checkout.jsp" class="btn-primary btn-block">Procedi all'ordine</a>
		
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