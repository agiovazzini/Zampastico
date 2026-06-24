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