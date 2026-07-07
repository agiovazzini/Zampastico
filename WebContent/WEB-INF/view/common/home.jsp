<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.UtenteBEAN" %>
<%@ page import="model.ProdottoBEAN" %>
<%@ page import="model.CategoriaBEAN" %>
<% 
    UtenteBEAN utente = (UtenteBEAN) session.getAttribute("utenteLoggato");
    boolean isLoggato = (utente != null);
    List<ProdottoBEAN> prodottiHome = (List<ProdottoBEAN>) request.getAttribute("prodottiHome");
    List<CategoriaBEAN> categorieHome = (List<CategoriaBEAN>) request.getAttribute("categorieHome");
%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="common-head.jsp" %>
    <title>Zampastico - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    
    <jsp:include page="header.jsp" />
    
    <main class="home-container">
    
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
        </div>
    <% } else { %>
        <div style="margin-top: 20px; padding: 20px; background-color: #f1ecf7; border-radius: 16px; text-align: center;">
            <h3 style="color: #333333; margin-bottom: 5px;">Non sei autenticato</h3>
            <br>
            <a href="${pageContext.request.contextPath}/login" class="btn-primary" style="padding: 10px 25px; text-decoration: none;">Vai alla pagina di Login</a>
        </div>
    <% } %>
    
    <section class="hero-section">
        <div class="hero-content">
            <h1>Il meglio per loro, <br>scelto con <span>amore</span><i class="fa-solid fa-paw paw-icon"></i></h1>
            <p>Scopri migliaia di prodotti di qualità per il benessere dei tuoi animali.</p>
            <a href="${pageContext.request.contextPath}/catalog" class="btn-primary hero-btn">
                Scopri il catalogo <i class="fa-solid fa-paw"></i>
            </a>
        </div>
        <div class="hero-image">
            <img src="${pageContext.request.contextPath}/images/assets/cane_e_gatto_new.png" alt="Cane e Gatto felici" onerror="this.src='https://via.placeholder.com/600x400?text=Cane+e+Gatto'">
        </div>
    </section>
    
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
    
    <section class="categories-section">
        <div class="section-header">
            <h2>Esplora le categorie <i class="fa-solid fa-paw" style="color: var(--color-primary);"></i></h2>
            <a href="${pageContext.request.contextPath}/catalog" class="link-view-all">Vedi tutte <i class="fa-solid fa-arrow-right"></i></a>
        </div>
        <div class="categories-grid">
        <% 
            if (categorieHome != null && !categorieHome.isEmpty()) {
                String[] bgColors = {"bg-light-green", "bg-light-orange", "bg-light-purple", "bg-light-orange", "bg-light-green", "bg-light-purple"};
                String[] textColors = {"text-green", "text-orange", "text-purple", "text-orange", "text-green", "text-purple"};
                String[] defaultIcons = {"fa-paw", "fa-cat", "fa-dog", "fa-bowl-food", "fa-ring", "fa-bone"};
                int idx = 0;
                for (CategoriaBEAN cat : categorieHome) {
                    String bg = bgColors[idx % bgColors.length];
                    String txt = textColors[idx % textColors.length];
                    String icon = defaultIcons[idx % defaultIcons.length];
                    
                    String nomeCat = cat.getNome() != null ? cat.getNome() : "";
                    String imgCat;
                    
                    // Verifica dinamica dell'immagine associata alla categoria tramite il backend
                    if (cat.getIdProdottoImmagine() != null && cat.getIdProdottoImmagine() > 0) {
                        imgCat = request.getContextPath() + "/image?action=show&code=" + cat.getIdProdottoImmagine();
                    } else {
                        imgCat = request.getContextPath() + "/images/assets/placeholder_0.png";
                    }
                    
                    // Assegnazione dell'icona estetica in base al nome (se corrispondente)
                    if (nomeCat.equalsIgnoreCase("Cani")) {
                        icon = "fa-dog";
                    } else if (nomeCat.equalsIgnoreCase("Gatti")) {
                        icon = "fa-cat";
                    } else if (nomeCat.equalsIgnoreCase("Cibo")) {
                        icon = "fa-bowl-food";
                    } else if (nomeCat.equalsIgnoreCase("Accessori")) {
                        icon = "fa-ring";
                    } else if (nomeCat.equalsIgnoreCase("Giochi")) {
                        icon = "fa-bone";
                    } else if (nomeCat.equalsIgnoreCase("Altri Animali")) {
                        icon = "fa-paw";
                    }
                    idx++;
        %>
            <a href="${pageContext.request.contextPath}/catalog?categoria=<%= cat.getIdCategoria() %>" class="category-card">
                <div class="category-img-wrap <%= bg %>">
                    <img src="<%= imgCat %>" alt="<%= nomeCat %>" style="object-fit: cover; width: 100%; height: 100%;" onerror="this.src='${pageContext.request.contextPath}/images/assets/placeholder_0.png'">
                </div>
                <span class="category-name <%= txt %>"><%= nomeCat %></span>
            </a>
        <% 
                }
            } else { 
        %>
        <% } %>
        </div>
    </section>
    
    <section class="popular-products-section">
        <div class="section-header">
            <h2>I più amati <i class="fa-solid fa-heart" style="color: var(--color-primary);"></i></h2>
            <a href="${pageContext.request.contextPath}/catalog" class="link-view-all">Vedi tutti i prodotti <i class="fa-solid fa-arrow-right"></i></a>
        </div>
        <div class="products-grid">
        <% 
           if (prodottiHome != null && !prodottiHome.isEmpty()) {
                for (ProdottoBEAN p : prodottiHome) { 
                    String prezzoForm = String.format("%.2f", p.getPrezzo()).replace(".", ",");
                    String nomeSafe = p.getNome() != null ? p.getNome().replace("'", "\\'") : "Prodotto";
                    String imgUrl = (p.getPath() != null && !p.getPath().trim().isEmpty())
                        ? request.getContextPath() + "/image?action=show&code=" + p.getId()
                        : request.getContextPath() + "/images/assets/placeholder_0.png";
        %>
                    <div class="product-card">
                        <% if (isLoggato) { %>
                            <button class="btn-wishlist-top" onclick="aggiungiPreferito('<%= nomeSafe %>', <%= p.getPrezzo() %>, '<%= imgUrl %>')" title="Aggiungi ai preferiti">
                               <i class="fa-regular fa-heart"></i>
                            </button>
                        <% } else { %>  
                           <button class="btn-wishlist-top" onclick="alert('Devi effettuare l\'accesso!'); window.location.href='${pageContext.request.contextPath}/login';" title="Aggiungi ai preferiti">
                                <i class="fa-regular fa-heart"></i>
                            </button>
                        <% } %>

                       <div class="product-image">
                            <img src="<%= imgUrl %>" alt="<%= p.getNome() %>" onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/assets/placeholder_0.png'">
                        </div>
                        <div class="product-info">
                            <h3 class="prod-title"><%= p.getNome() %></h3>
                            <p style="font-size: 0.8rem; color: #888; margin-bottom: 5px;"><%= p.getMarca() != null ? p.getMarca() : "" %></p>
                            <div class="prod-rating">
                                <i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star-half-stroke"></i>
                            </div>
                            <div class="prod-bottom">
                                <span class="prod-price"><%= prezzoForm %>€</span>
                                <button class="btn-add-cart" onclick="aggiungiAlCarrello('<%= nomeSafe %>', <%= p.getPrezzo() %>, '<%= imgUrl %>')" title="Aggiungi al carrello">
                                    <i class="fa-solid fa-cart-shopping"></i>
                                </button>
                          </div>
                        </div>
                    </div>
        <% 
                }
            } else { 
        %>
            <div style="grid-column: 1 / -1; text-align: center; padding: 40px;">
                <i class="fa-solid fa-box-open" style="font-size: 3rem; color: #ccc; margin-bottom: 15px;"></i>
                <h3 style="color: #555;">Nessun prodotto in evidenza</h3>
                <p style="color: #777;">Al momento non ci sono prodotti da mostrare nel catalogo.</p>
            </div>
        <% } %>
        </div>
    </section>
    
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
            <img src="${pageContext.request.contextPath}/images/assets/besties_new.png" alt="Pets" onerror="this.style.display='none'">
        </div>
    </section>
    
    </main>
 
    <jsp:include page="footer.jsp" />
 
    <script src="${pageContext.request.contextPath}/scripts/main.js?v=2"></script>
 
</body>
</html>