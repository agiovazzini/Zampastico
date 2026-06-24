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

<main class="cart-container">
	<h1 class="cart-title">Il tuo carrello 🐾</h1>
	
	<div class="cart-layout">
		<div class="cart-items-section">
			<div class="cart-item-card">
				<div class="cart-item-image">
					<img src="" onerror="">
				</div>
				<div class="cart-item-details">
					<span class="item-category">Cani</span>
					<h3 class="item-name">Monge Natural Superpremium</h3>
					<p class="item-weight">Crocchette per cani di taglia piccola</p>
				</div>
				<div class="cart-item-actions">
					<div class="qty-control">
						<button class="qty-btn">-</button>
						<input type="number" value="1" min="1" class="qty-input" readonly>
						<button class="qty-btn">+</button>
					</div>
				</div>
				<div class="cart-item-price-wrap">
					<span class="item-price">50,99€</span>
					<button class="remove-btn" title="Rimuovi dal carrello"><i class="fa-solid fa-trash-can"></i></button>
				</div>
			</div>
			
			<!-- Secondo prodotto -->
			<div class="cart-item-card">
				<div class="cart-item-image">
					<img src="" onerror="">
				</div>
				<div class="cart-item-details">
					<span class="item-category">Giochi</span>
					<h3 class="item-name">Palla in corda</h3>
					<p class="item-weight">Gioco per cani.</p>
				</div>
				<div class="cart-item-actions">
					<div class="qty-control">
						<button class="qty-btn">-</button>
						<input type="number" value="2" min="1" class="qty-input" readonly>
						<button class="qty-btn">+</button>
					</div>
				</div>
				<div class="cart-item-price-wrap">
					<span class="item-price">16,80€</span>
					<button class="remove-btn" title="Rimuovi dal carrello"><i class="fa-solid fa-trash-can"></i></button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Colonna destra: Riepilogo Ordine -->
	<div class="cart-summary-section">
		<h3 class="summary-title">Riepilogo Ordine</h3>

		<div class="summary-row">
			<span>Subtotale (3 articoli)</span>
			<span>46,70€</span>
		</div>

		<div class="summary-row">
			<span>Spedizione</span>
			<span class="free-shipping">Gratuita</span>
		</div>
		
		<hr class="summary-divider">
		
		<div class="summary-row total-row">
			<span>Totale:</span>
			<span>46,70€</span>
		</div>
		
		<a href="${pageContext.request.contextPath}/checkout.jsp" class="btn-primary btn-block">Procedi all'ordine</a>
		
		<div class="secure-checkout">
			<i class="fa-solid fa-shield-halved"></i> Pagamento Sicuro
		</div>
	</div>
</main>

<!-- Inclusione nel footer -->
<jsp:include page="footer.jsp" />

<!-- Inclusione dello script JS -->
<script src="${pageContext.request.contextPath}/scripts/main.js"></script>
</body>
</html>