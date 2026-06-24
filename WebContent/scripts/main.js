window.addEventListener('pageshow', function(event) {
    if (event.persisted || (window.performance && window.performance.getEntriesByType("navigation")[0].type === "back_forward")) {
        window.location.reload(true);
    }
});

document.addEventListener('DOMContentLoaded', function() {
    aggiornaPallinoCarrello();

    const bottoniAggiungi = document.querySelectorAll('.btn-add-cart');
    
    bottoniAggiungi.forEach(bottone => {
        bottone.onclick = function(e) {
            e.preventDefault(); 

            const card = this.closest('.product-card');
            
            const nome = card.querySelector('.prod-title').innerText;
            const prezzoTesto = card.querySelector('.prod-price').innerText; 
            const prezzo = parseFloat(prezzoTesto.replace('€', '').replace(',', '.').trim());
            const immagine = card.querySelector('.product-image img').src;

            let carrello = JSON.parse(localStorage.getItem('zampastico_cart')) || [];
            
            const prodottoEsistente = carrello.find(item => item.nome === nome);
            if (prodottoEsistente) {
                prodottoEsistente.quantita += 1;
            } else {
                carrello.push({ nome: nome, prezzo: prezzo, immagine: immagine, quantita: 1 });
            }

            localStorage.setItem('zampastico_cart', JSON.stringify(carrello));
            
            aggiornaPallinoCarrello();
            
            alert(nome + " aggiunto al carrello! 🐾");
            
            const vecchioColore = this.style.backgroundColor;
            this.style.backgroundColor = '#4caf50'; 
            setTimeout(() => this.style.backgroundColor = vecchioColore, 500);
        };
    });
});

window.aggiornaPallinoCarrello = function() {
    const carrello = JSON.parse(localStorage.getItem('zampastico_cart')) || [];
    let totaleArticoli = 0;
    
    // Filtro che conta solo gli articoli validi
    carrello.forEach(item => {
        if(item && item.quantita) {
            totaleArticoli += item.quantita;
        }
    });
    
    const pallini = document.querySelectorAll('.cart-badge');
    pallini.forEach(pallino => {
        pallino.innerText = totaleArticoli;
        pallino.style.display = totaleArticoli > 0 ? 'flex' : 'none';
    });
};

//PER LA WISHLIST (Lista dei Desideri)

// 1. Aggiunge ai preferiti registrando la DATA DI OGGI
window.aggiungiPreferito = function(nome, prezzo, immagine) {
    let wishlist = JSON.parse(localStorage.getItem('zampastico_wishlist')) || [];
    const esisteGia = wishlist.find(item => item.nome === nome);
    
    if (esisteGia) {
        alert("Questo prodotto è già nei tuoi preferiti! 🐾");
    } else {
        wishlist.push({ 
            nome: nome, 
            prezzo: prezzo, 
            immagine: immagine,
            dataAggiunta: new Date().getTime() // Registra il millisecondo esatto dell'aggiunta
        });
        localStorage.setItem('zampastico_wishlist', JSON.stringify(wishlist));
        alert(nome + " aggiunto ai preferiti! Cliccando sul cuoricino in alto potrai decidere se acquistarlo. ❤️");
    }
};

// 2. Disegna la pagina filtrando i prodotti scaduti
window.caricaWishlist = function() {
    let wishlist = JSON.parse(localStorage.getItem('zampastico_wishlist')) || [];
    const contenitore = document.getElementById('wishlist-content');
    const messaggioVuoto = document.getElementById('empty-wishlist');
    
    if (!contenitore) return;

    if (wishlist.length === 0) {
        contenitore.style.display = 'none';
        messaggioVuoto.style.display = 'flex';
        return;
    }

    contenitore.style.display = 'grid'; 
    messaggioVuoto.style.display = 'none';
    
    let html = '';
    wishlist.forEach((prodotto, index) => {
        html += `
        <div class="product-card">
            <div class="product-image"><img src="${prodotto.immagine}" alt="${prodotto.nome}"></div>
            <h3 class="prod-title">${prodotto.nome}</h3>
            <div class="prod-bottom">
                <span class="prod-price">${parseFloat(prodotto.prezzo).toFixed(2).replace('.', ',')}€</span>
                <div style="display: flex; gap: 8px;">
                    <button class="btn-remove-wishlist" onclick="rimuoviDaWishlist(${index})"><i class="fa-solid fa-trash-can"></i></button>
                    <button class="btn-add-cart" onclick="spostaNelCarrello(${index})"><i class="fa-solid fa-cart-plus"></i></button>
                </div>
            </div>
        </div>
        `;
    });
    contenitore.innerHTML = html;
};

// 3. Rimuove un elemento manualmente
window.rimuoviDaWishlist = function(index) {
    let wishlist = JSON.parse(localStorage.getItem('zampastico_wishlist')) || [];
    wishlist.splice(index, 1);
    localStorage.setItem('zampastico_wishlist', JSON.stringify(wishlist));
    caricaWishlist(); 
};

// 4. La "Decisione": Sposta il prodotto nel Carrello e lo toglie dai Preferiti
window.spostaNelCarrello = function(index) {
    let wishlist = JSON.parse(localStorage.getItem('zampastico_wishlist')) || [];
    
    if (index >= 0 && index < wishlist.length) {
        const prodottoScelto = wishlist[index];

        // A. Lo aggiunge al Carrello
        let carrello = JSON.parse(localStorage.getItem('zampastico_cart')) || [];
        const prodottoEsistente = carrello.find(item => item.nome === prodottoScelto.nome);
        
        if (prodottoEsistente) {
            prodottoEsistente.quantita += 1;
        } else {
            carrello.push({ 
                nome: prodottoScelto.nome, 
                prezzo: parseFloat(prodottoScelto.prezzo), 
                immagine: prodottoScelto.immagine, 
                quantita: 1 
            });
        }
        localStorage.setItem('zampastico_cart', JSON.stringify(carrello));
        if(typeof aggiornaPallinoCarrello === 'function') aggiornaPallinoCarrello();

        // B. Lo RIMUOVE dai Preferiti (perché la decisione è stata presa)
        wishlist.splice(index, 1);
        localStorage.setItem('zampastico_wishlist', JSON.stringify(wishlist));
        
        // C. Ricarica la grafica della pagina
        caricaWishlist();
        
        alert(prodottoScelto.nome + " è stato spostato nel carrello! 🛒");
    }
};