<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.UtenteBEAN" %>
<% UtenteBEAN utente = (UtenteBEAN) session.getAttribute("utenteLoggato"); %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common-head.jsp" %>
    <title>Zampastico - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
	
	<!-- Header -->
	<jsp:include page="header.jsp" />
	
	<main class="home-container">
	
	<!-- GESTIONE SESSIONE E LOGIN -->
    <% String infoMsg = (String) request.getAttribute("statoAccesso"); %>
    <% if (infoMsg != null) { %>
        <div style="margin-top: 20px; padding: 15px; background-color: #eaf5eb; color: #518544; border-radius: 16px; text-align: center; font-weight: bold;">
            <%= infoMsg %>
        </div>
    <% } %>
    
    <% if (utente != null) { %>
        <div style="margin-top: 20px; padding: 20px; background-color: #fcece5; border-radius: 16px; text-align: center;">
            <h3 style="color: #333333; margin-bottom: 5px;">Ciao, <%= utente.getNome() %> <%= utente.getCognome() %>!🐾</h3>
            <p style="color: #777777; margin-bottom: 15px;">La tua email: <%= utente.getEmail() %></p>
            <a href="${pageContext.request.contextPath}/logout" class="btn-primary" style="padding: 10px 25px; text-decoration: none;">Esci dall'account</a>
            <br>
        </div>
        
    <% } else { %>
        <div style="margin-top: 20px; padding: 20px; background-color: #f1ecf7; border-radius: 16px; text-align: center;">
            <h3 style="color: #333333; margin-bottom: 5px;">Non sei autenticato</h3>
            <br>
            <a href="${pageContext.request.contextPath}/login" class="btn-primary" style="padding: 10px 25px; text-decoration: none;">Vai alla pagina di Login</a>
        </div>
    <% } %>
    <script src="${pageContext.request.contextPath}/scripts/main.js"></script>
    
    <!-- Hero section -->
    <section class="hero-section">
    	<div class="hero-content">
    		<h1>Il meglio per loro, <br>scelto con <span>amore</span><i class="fa-solid fa-paw paw-icon"></i></h1>
    		<p>Scopri migliaia di prodotti di qualità per il benessere dei tuoi animali.</p>
    		<a href="${pageContext.request.contextPath}/catalogo" class="btn-primary hero-btn">
    			Scopri il catalogo <i class="fa-solid fa-paw"></i>
    		</a>
    	</div>
    	<div class="hero-image">
    		<img src="${pageContext.request.contextPath}/images/assets/hero-pets.png" alt="Cane e Gatto felici" onerror="this.src='https://via.placeholder.com/600x400?text=Cane+e+Gatto'">
    	</div>
    </section>
    
    <!-- Features Banner -->
    <section class="features-banner">
    	<div class="feature-item">
    		<div class="feature-icon bg-light-green"><i class="fa-solid fa-truck"></i></div>
    		<div class="feature-text">
    			<h4>Spedizione veloce</h4>
    			<p>Consegna in 24/48h<br>in tutta Italia</p>
    		</div>
    	</div>
    	<div class="feature-item">
    		<div class="feature-icon bg-light-orange"><i class="fa-solid fa-box-open"></i></div>
    		<div class="feature-text">
    			<h4>Reso facile</h4>
    			<p>Hai 14 giorni per<br>cambiare idea</p>
    		</div>
    	</div>
    	<div class="feature-item">
    		<div class="feature-icon bg-light-green"><i class="fa-solid fa-leaf"></i></div>
    		<div class="feature-text">
    			<h4>Prodotti di qualità</h4>
    			<p>Selezioniamo solo il<br>meglio per loro</p>
    		</div>
    	</div>
    	<div class="feature-item">
    		<div class="feature-icon bg-light-orange"><i class="fa-solid fa-headset"></i></div>
    		<div class="feature-text">
    			<h4>Servizio clienti</h4>
    			<p>Siamo qui per te e<br>per i tuoi amici a quattro zampe</p>
    		</div>	
    	</div>
    </section>
    
    <!-- CATEGORIE -->
    <section class="categories-section">
    	<div class="section-header">
    		<h2>Esplora le categorie <i class="fa-solid fa-paw" style="color: var(--color-primary);"></i></h2>
    		<a href="${pageContext.request.contextPath}/catalogo" class="link-view-all">Vedi tutte <i class="fa-solid fa-arrow-right"></i></a>
    	</div>
    	<div class="categories-grid">
    		<a href="#" class="category-card">
    			<div class="category-img-wrap bg-light-green">
    				<img src="https://via.placeholder.com/150?text=Cane" alt="Cani">
    				<div class="cat-icon text-green"><i class="fa-solid fa-dog"></i></div>
    			</div>
    			<span class="category-name text-green">Cani</span>
    		</a>
    		
    		<a href="#" class="category-card">
    			<div class="category-img-wrap bg-light-orange">
    				<img src="https://via.placeholder.com/150?text=Gatto" alt="Gatti">
    				<div class="cat-icon text-green"><i class="fa-solid fa-cat"></i></div>
    			</div>
    			<span class="category-name text-green">Gatti</span>
    		</a>
 
			<a href="#" class="category-card">
                <div class="category-img-wrap bg-light-purple">
                    <img src="https://via.placeholder.com/150?text=Coniglio" alt="Altri Animali">
                    <div class="cat-icon text-purple"><i class="fa-solid fa-paw"></i></div>
                </div>
                <span class="category-name text-purple">Altri Animali</span>
            </a>
            
            <a href="#" class="category-card">
            	<div class="category-img-wrap bg-light-orange">
            		<img src="https://via.placeholder.com/150?text=Cibo" alt="Cibo">
            		<div class="cat-icon text-orange"><i class="fa-solid fa-bowl-food"></i></div>
            	</div>
            	<span class="category-name text-orange">Cibo</span>
            </a>
            
            <a href="#" class="category-card">
            	<div class="category-img-wrap bg-light-green">
            		<img src="https://via.placeholder.com/150?text=Collare" alt="Accessori">
            		<div class="cat-icon text-green"><i class="fa-solid fa-ring"></i></div>
            	</div>
            	<span class="category-name text-green">Accessori</span>
            </a>
            
            <a href="#" class="category-card">
            	<div class="category-img-wrap bg-light-orange">
            		<img src="https://via.placeholder.com/150?text=Gioco" alt="Giochi">
            		<div class="cat-icon text-orange"><i class="fa-solid fa-bone"></i></div>
            	</div>
            	<span class="category-name text-orange">Giochi</span>
            </a>
    	
    	</div>
    </section>
    
    <!-- PRODOTTI -->
    <section class="popular-products-section">
    	<div class="section-header">
    		<h2>I più amati <i class="fa-solid fa-heart" style="color: var(--color-primary);"></i></h2>
    		<a href="${pageContext.request.contextPath}/catalogo" class="link-view-all">Vedi tutti i prodotti <i class="fa-solid fa-arrow-right"></i></a>
    	</div>
    	
    	<div class="products-grid">
    		<div class="product-card">
    			<div class="product-image">
    				<img src="${pageContext.request.contextPath}/images/assets/monge-salmone.jpg" alt="Crocchette Monge Salmone per cani" onerror="this.src='https://via.placeholder.com/200x200?text=Monge+Salmone'">
    			</div>
    			<div class="product-info">
    				<h3 class="prod-title">Monge Natural Superpremium</h3>
    				<p class="prod-desc">Crocchette per cani</p>
    				<div class="prod-rating">
    					<i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star-half-stroke"></i> <span>(142)</span>
    				</div>
    				<div class="prod-bottom">
    					<span class="prod-price">50,99€</span>
    					<button class="btn-add-cart"><i class="fa-solid fa-cart-shopping"></i></button>
    				</div>
    			</div>
    		</div>
    		
    		<div class="product-card">
    			<span class="badge badge-new">Novità</span>
    			<div class="product-image">
    				<img src="https://via.placeholder.com/200x200?text=Cuccia" alt="Cuccia">
    			</div>
    			<div class="product-info">
    				<h3 class="prod-title">Cuccia Morbiba Deluxe</h3>
    				<p class="prod-desc">per cani e per gatti</p>
    				<div class="prod-rating">
    					<i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i> <span>(86)</span>
    				</div>
    				<div class="prod-bottom">
    					<span class="prod-price">34,90 €</span>
    					<button class="btn-add-cart"><i class="fa-solid fa-cart-shopping"></i></button>
    				</div>
    			</div>
    		</div>
    		<div class="product-card">
    			<span class="badge badge-sale">-15%</span>
    			<div class="product-image">
    				<img src="https://via.placeholder.com/200x200?text=Gioco" alt="Palla">
    			</div>
    			<div class="product-info">
    				<h3 class="prod-title">Palla in corda</h3>
    				<p class="prod-desc">Gioco per cani</p>
    				<div class="prod-rating">
    					<i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-regular fa-star"></i> <span>(54)</span>
    				</div>
    				<div class="prod-bottom">
    					<span class="prod-price text-sale">8,40 € <del>9,90 €</del></span>
    					<button class="btn-add-cart"><i class="fa-solid fa-cart-shopping"></i></button>
    				</div>
    			</div>
    		</div>
    		<div class="product-card">
    			<div class="product-image">
    				<img src="https://via.placeholder.com/200x200?text=Frontline" alt="Frontline">
    			</div>
    			<div class="product-info">
    				<h3 class="prod-title">Frontline Combo Gatto</h3>
    				<p class="prod-desc">Antiparassitario</p>
    				<div class="prod-rating">
    					<i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i> <span>(102)</span>
    				</div>
    				<div class="prod-bottom">
    					<span class="prod-price">23,90 €</span>
    					<button class="btn-add-cart"><i class="fa-solid fa-cart-shopping"></i></button>
    				</div>
    			</div>
    		</div>
    	</div>
    </section>
    
    <!-- NEWSLETTER -->
    <section class="newsletter-section">
    	<div class="news-icon-bg"><i class="fa-solid fa-paw"></i></div>
    	<div class="news-text">
    		<h2>Iscriviti anche tu alla ZampaCommunity!🐾</h2>
    		<p>Ricevi offerte esclusive, consigli utili e tante coccole per i tuoi amici a quattro zampe!</p>
    	</div>
    	<div class="news-action">
    		<a href="#" class="btn-primary">Iscriviti ora</a>
    	</div>
    	<div class="news-image">
    		<img src="${pageContext.request.contextPath}/images/assets/newsletter-pets.png" alt="Pets" onerror="this.style.display='none'">
    	</div>
    </section>
    
 </main>
 
 <!-- Aggiunta del footer -->
 <jsp:include page="footer.jsp" />
 
 <!-- Script JavaScript -->
 <script src="${pageContext.request.contextPath}/scripts/main.js"></script>
 
</body>
</html>