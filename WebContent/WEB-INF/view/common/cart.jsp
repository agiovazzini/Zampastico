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
    const cartItemsSection = document.querySelector('.cart-items-section');
    const btnProcedi = document.querySelector('.btn-block'); // Il bottone "Procedi all'ordine"
    
    // Funzione principale che legge la memoria e disegna la pagina
    function renderizzaCarrello() {
        let carrello = JSON.parse(localStorage.getItem('zampastico_cart')) || [];

        // SE IL CARRELLO E' VUOTO: Mostra il bel messaggio grafico
        if(carrello.length === 0) {
            cartItemsSection.innerHTML = `
                <div class="empty-cart-message">
                    <i class="fa-solid fa-basket-shopping empty-icon"></i>
                    <h2>Il tuo carrello è vuoto! 🐾</h2>
                    <p>Non hai ancora scelto nessun prodotto per il tuo amico a 4 zampe.</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn-primary">Inizia lo shopping</a>
                </div>
            `;
            document.getElementById('summary-count').textContent = 'Subtotale (0 articoli)';
            document.getElementById('summary-subtotal').textContent = '0,00€';
            document.getElementById('summary-total').textContent = '0,00€';
            
            // Disattiva il tasto "Procedi all'ordine"
            btnProcedi.style.opacity = '0.5';
            btnProcedi.style.pointerEvents = 'none';
            return;
        }

        // SE CI SONO PRODOTTI: Costruisce l'HTML per ogni prodotto
        let htmlProdotti = '';
        let totaleEuro = 0;
        let totaleArticoli = 0;

        carrello.forEach((prodotto, index) => {
            const totaleRiga = prodotto.prezzo * prodotto.quantita;
            totaleEuro += totaleRiga;
            totaleArticoli += prodotto.quantita;

            htmlProdotti += `
                <div class="cart-item-card">
                    <div class="cart-item-image">
                        <img src="\${prodotto.immagine}" alt="\${prodotto.nome}">
                    </div>
                    <div class="cart-item-details">
                        <h3 class="item-name">\${prodotto.nome}</h3>
                        <p class="item-weight">Prezzo singolo: \${prodotto.prezzo.toFixed(2).replace('.', ',')}€</p>
                    </div>
                    <div class="cart-item-actions">
                        <div class="qty-control">
                            <button class="qty-btn" onclick="cambiaQuantita(\${index}, -1)">-</button>
                            <input type="number" value="\${prodotto.quantita}" class="qty-input" readonly>
                            <button class="qty-btn" onclick="cambiaQuantita(\${index}, 1)">+</button>
                        </div>
                    </div>
                    <div class="cart-item-price-wrap">
                        <span class="item-price">\${totaleRiga.toFixed(2).replace('.', ',')}€</span>
                        <button class="remove-btn" onclick="rimuoviProdotto(\${index})"><i class="fa-solid fa-trash-can"></i></button>
                    </div>
                </div>
                `;
            });

        // Inserisce tutto l'HTML generato nella pagina
        cartItemsSection.innerHTML = htmlProdotti;

        // Aggiorna la colonna di destra con i totali
        document.getElementById('summary-count').textContent = 'Subtotale (' + totaleArticoli + ' articoli)';
        document.getElementById('summary-subtotal').textContent = totaleEuro.toFixed(2).replace('.', ',') + '€';
        document.getElementById('summary-total').textContent = totaleEuro.toFixed(2).replace('.', ',') + '€';
        
        // Riattiva il bottone "Procedi all'ordine"
        btnProcedi.style.opacity = '1';
        btnProcedi.style.pointerEvents = 'auto';

        // Sincronizza anche il pallino dell'header
        if(typeof aggiornaPallinoCarrello === 'function') aggiornaPallinoCarrello();
    }

    // --- FUNZIONI PER I BOTTONI (+, -, Cestino) ---
    window.cambiaQuantita = function(index, delta) {
        let carrello = JSON.parse(localStorage.getItem('zampastico_cart'));
        // Evita che la quantità vada a 0 o sotto
        if (carrello[index].quantita + delta >= 1) {
            carrello[index].quantita += delta;
            localStorage.setItem('zampastico_cart', JSON.stringify(carrello));
            renderizzaCarrello(); // Ridisegna il carrello aggiornato
        }
    };

    window.rimuoviProdotto = function(index) {
        let carrello = JSON.parse(localStorage.getItem('zampastico_cart'));
        carrello.splice(index, 1); // Rimuove l'elemento dall'array
        localStorage.setItem('zampastico_cart', JSON.stringify(carrello));
        renderizzaCarrello(); // Ridisegna il carrello aggiornato
    };

    // Avvia la costruzione del carrello appena apri la pagina
    renderizzaCarrello();
});
</script>

<!-- Inclusione nel footer -->
<jsp:include page="footer.jsp" />

<!-- Inclusione dello script JS -->
<script src="${pageContext.request.contextPath}/scripts/main.js"></script>
</body>
</html>