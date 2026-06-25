<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.ProdottoBEAN" %>
<%@ page import="model.UtenteBEAN" %>
<% 
    // 1. Controlla se l'utente è loggato (serve per la wishlist)
    boolean isLoggato = (session.getAttribute("utenteLoggato") != null); 
    
    // 2. Recupera la lista dei prodotti dal database passata dalla Servlet
    List<ProdottoBEAN> listaProdotti = (List<ProdottoBEAN>) request.getAttribute("prodottiCatalogo"); 
%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="common-head.jsp" %>
    <title>Catalogo - Zampastico</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <jsp:include page="header.jsp" />

    <main class="catalog-container container">
        <div class="catalog-header">
            <span class="breadcrumbs">Home > Tutti i prodotti</span>
            <h1>Il nostro Catalogo <i class="fa-solid fa-paw" style="color: #f37925;"></i></h1>
        </div>

        <div class="catalog-layout">
            
            <aside class="catalog-sidebar">
                <h3 class="sidebar-title"><i class="fa-solid fa-filter"></i> Filtra per</h3>
                
                <div class="filter-group">
                    <h4>Categoria</h4>
                    <label class="filter-checkbox"><input type="checkbox"> Cani</label>
                    <label class="filter-checkbox"><input type="checkbox"> Gatti</label>
                    <label class="filter-checkbox"><input type="checkbox"> Altri Animali</label>
                </div>
                
                <div class="filter-group">
                    <h4>Marca</h4>
                    <label class="filter-checkbox"><input type="checkbox"> Monge</label>
                    <label class="filter-checkbox"><input type="checkbox"> Royal Canin</label>
                    <label class="filter-checkbox"><input type="checkbox"> Frontline</label>
                    <label class="filter-checkbox"><input type="checkbox"> Natural Trainer</label>
                </div>
                
                <div class="filter-group">
                    <h4>Prezzo</h4>
                    <div class="price-inputs">
                        <input type="number" placeholder="Min €" class="price-box">
                        <span>-</span>
                        <input type="number" placeholder="Max €" class="price-box">
                    </div>
                </div>
                
                <button class="btn-primary" style="width: 100%; margin-top: 15px; padding: 10px;">Applica Filtri</button>
            </aside>

            <div class="catalog-main">
                
                <div class="catalog-toolbar">
                    <span class="results-count">Mostrando i risultati del database</span>
                    <div class="sort-box">
                        <label>Ordina per:</label>
                        <select class="custom-select">
                            <option>Più Rilevanti</option>
                            <option>Prezzo: Crescente</option>
                            <option>Prezzo: Decrescente</option>
                            <option>Novità</option>
                        </select>
                    </div>
                </div>

                <div class="catalog-grid">
                    
                    <% 
                    // Se la lista NON è vuota, avvia il ciclo per stampare le card
                    if (listaProdotti != null && !listaProdotti.isEmpty()) {
                        for(ProdottoBEAN p : listaProdotti) { 
                            // Formattiamo il prezzo con 2 decimali e la virgola
                            String prezzoForm = String.format("%.2f", p.getPrezzo()).replace(".", ",");
                            
                            // Rendiamo i nomi sicuri per JavaScript (sostituiamo eventuali apici)
                            String nomeSafe = p.getNome() != null ? p.getNome().replace("'", "\\'") : "Prodotto";
                            String imgPath = p.getImmagine() != null ? p.getImmagine() : "images/assets/placeholder.png";
                    %>
                            <div class="product-card">
                                
                                <% if (isLoggato) { %>
                                    <button class="btn-wishlist-top" onclick="aggiungiPreferito('<%= nomeSafe %>', <%= p.getPrezzo() %>, '${pageContext.request.contextPath}/<%= imgPath %>')" title="Aggiungi ai preferiti">
                                        <i class="fa-regular fa-heart"></i>
                                    </button>
                                <% } else { %>  
                                    <button class="btn-wishlist-top" onclick="alert('Devi effettuare l\'accesso!'); window.location.href='${pageContext.request.contextPath}/login';" title="Aggiungi ai preferiti">
                                        <i class="fa-regular fa-heart"></i>
                                    </button>
                                <% } %>

                                <div class="product-image">
                                    <img src="${pageContext.request.contextPath}/<%= imgPath %>" alt="<%= p.getNome() %>" onerror="this.onerror=null; this.src='https://via.placeholder.com/200x200?text=Immagine+non+trovata'">
                                </div>
                                <div class="product-info">
                                    <h3 class="prod-title"><%= p.getNome() %></h3>
                                    
                                    <p style="font-size: 0.8rem; color: #888; margin-bottom: 5px;"><%= p.getMarca() %></p>
                                    
                                    <div class="prod-rating"><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star-half-stroke"></i></div>
                                    <div class="prod-bottom">
                                        <span class="prod-price"><%= prezzoForm %>€</span>
                                        <button class="btn-add-cart" onclick="aggiungiAlCarrello('<%= nomeSafe %>', <%= p.getPrezzo() %>, '${pageContext.request.contextPath}/<%= imgPath %>')" title="Aggiungi al carrello">
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
                            <h3 style="color: #555;">Nessun prodotto trovato</h3>
                            <p style="color: #777;">Al momento il catalogo è vuoto. Torna a trovarci presto!</p>
                        </div>
                    <% } %>
                    
                </div>

                <div class="pagination">
                    <button class="page-btn active">1</button>
                    <button class="page-btn">2</button>
                    <button class="page-btn">3</button>
                    <button class="page-btn"><i class="fa-solid fa-chevron-right"></i></button>
                </div>

            </div> 
        </div> 
    </main>

    <jsp:include page="footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/scripts/main.js?v=2"></script>
</body>
</html>