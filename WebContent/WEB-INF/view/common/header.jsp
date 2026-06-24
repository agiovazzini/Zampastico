<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common-head.jsp" %>
<title></title>
</head>
<body>

<!-- BANNER SPEDIZIONE -->
<div class="top-banner">
	<i class="fa-solid fa-truck-fast"></i> Spedizione gratuita da 39€ di spesa!
</div>

<!-- HEADER PRINCIPALE -->
<header class="top-section container">
	<!-- AREA, LOGO E SOTTOTITOLO -->
	<div class="div-logo">
		<a href="${pageContext.request.contextPath}/home">
			<img src="${pageContext.request.contextPath}/images/assets/logo_nuovo.png" alt="Zampastico">
		</a>
		<span class="logo-subtitle">Tutto ciò che rende felice il tuo amico a 4 zampe!</span>
	</div>
	
	<!-- AREA DI RICERCA -->
	<div class="div-search">
		<form action="RicercaServlet" method="GET" class="form-search">
			<input type="text" name="query" id="search" placeholder="Cerca prodotti..." required>
			<!-- Viene usata l'icona della lente di ingrandimento al posto del testo -->
			<button type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
		</form>
		<div id="search-suggestions"></div>
	</div>
	
	<!-- AREA AZIONI (ICONE A DESTRA) -->
	<div class="div-actions">
		<a href="#" class="action-element">
			<div class="action-picture">
				<i class="fa-regular fa-user"></i>
			</div>
			<span>Account</span>
		</a>
		
		<a href="${pageContext.request.contextPath}/wishlist" class="action-element">
    		<button class="action-picture"><i class="fa-regular fa-heart"></i></button>
			<span>Preferiti</span>
		</a>
		
		<a href="${pageContext.request.contextPath}/cart" class="action-element">
			<div class="action-picture cart-icon-wrapper">
				<i class="fa-solid fa-cart-shopping"></i>
				
				<!-- PALLINO ARANCIONE CON IL NUMERO DEGLI ARTICOLI -->
				<span class="cart-badge">0</span>
			</div>
			<span>Carrello</span>
		</a>
	</div>
</header>

<!-- MENU DI NAVIGAZIONE (NAVBAR) -->
<nav class="main-nav container">
	<ul>
		<li><a href="#">Cani</a></li>
		<li><a href="#">Gatti</a></li>
		<li><a href="#">Altri Animali</a></li>
		<li><a href="#">Cibo</a></li>
		<li><a href="#">Giochi</a></li>
		<li><a href="#">Accessori</a></li>
		<li><a href="#">Igiene</a></li>
		
		<!-- CLASSE PER COLORARE LE OFFERTE DI ARANCIONE -->
		<li><a href="#" class="text-orange">Offerte</a></li>
		<li><a href="#">Blog</a></li>
	</ul>
</nav>

</body>
</html>