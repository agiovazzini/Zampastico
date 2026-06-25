<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="common-head.jsp" %>
    <title>Catalogo - Zampastico</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>

    <!-- Header -->
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
                    <span class="results-count">Mostrando 1-12 di 150 prodotti</span>
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
                    
                    <div class="product-card">
                        <button class="btn-wishlist-top" title="Aggiungi ai preferiti"><i class="fa-regular fa-heart"></i></button>
                        <div class="product-image"><img src="${pageContext.request.contextPath}/images/assets/monge_salmone.png" alt="Monge"></div>
                        <div class="product-info">
                            <h3 class="prod-title">Monge Natural Superpremium</h3>
                            <div class="prod-rating"><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star-half-stroke"></i></div>
                            <div class="prod-bottom">
                                <span class="prod-price">50,99€</span>
                                <button class="btn-add-cart"><i class="fa-solid fa-cart-shopping"></i></button>
                            </div>
                        </div>
                    </div>
                    
                </div>

                <div class="pagination">
                    <button class="page-btn active">1</button>
                    <button class="page-btn">2</button>
                    <button class="page-btn">3</button>
                    <button class="page-btn"><i class="fa-solid fa-chevron-right"></i></button>
                </div>

            </div> </div> </main>

    <!-- Footer -->
    <jsp:include page="footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/scripts/main.js?v=2"></script>
</body>
</html>