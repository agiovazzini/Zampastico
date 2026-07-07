/* --- INIZIALIZZAZIONE E CARRELLO GLOBALE --- */
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
            mostraToast(nome + " aggiunto al carrello! 🐾");
            
            const vecchioColore = this.style.backgroundColor;
            this.style.backgroundColor = '#4caf50'; 
            setTimeout(() => this.style.backgroundColor = vecchioColore, 500);
        };
    });
});

window.aggiornaPallinoCarrello = function() {
    const carrello = JSON.parse(localStorage.getItem('zampastico_cart')) || [];
    let totaleArticoli = 0;
    
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

/* --- WISHLIST (LISTA DEI DESIDERI) --- */
window.aggiungiPreferito = function(nome, prezzo, immagine) {
    let wishlist = JSON.parse(localStorage.getItem('zampastico_wishlist')) || [];
    const esisteGia = wishlist.find(item => item.nome === nome);
    
    if (esisteGia) {
        mostraToast("Questo prodotto è già nei tuoi preferiti! 🐾");
    } else {
        wishlist.push({ 
            nome: nome, 
            prezzo: prezzo, 
            immagine: immagine,
            dataAggiunta: new Date().getTime()
        });
        localStorage.setItem('zampastico_wishlist', JSON.stringify(wishlist));
        mostraToast(nome + " aggiunto ai preferiti! ❤️");
    }
};

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

window.rimuoviDaWishlist = function(index) {
    let wishlist = JSON.parse(localStorage.getItem('zampastico_wishlist')) || [];
    wishlist.splice(index, 1);
    localStorage.setItem('zampastico_wishlist', JSON.stringify(wishlist));
    caricaWishlist(); 
};

window.spostaNelCarrello = function(index) {
    let wishlist = JSON.parse(localStorage.getItem('zampastico_wishlist')) || [];
    
    if (index >= 0 && index < wishlist.length) {
        const prodottoScelto = wishlist[index];

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

        wishlist.splice(index, 1);
        localStorage.setItem('zampastico_wishlist', JSON.stringify(wishlist));
        
        caricaWishlist();
        mostraToast(prodottoScelto.nome + " è stato spostato nel carrello! 🛒");
    }
};

/* --- VALIDAZIONE FORM REGISTRAZIONE --- */
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('regForm');
    
    if (!form) return; 

    const inputNome = document.getElementById('nome');
    const inputCognome = document.getElementById('cognome');
    const inputEmail = document.getElementById('email');
    const inputPassword = document.getElementById('password');

    const regexTesto = /^[a-zA-Z\s']{2,50}$/; 
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; 
    const regexPassword = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d!@#$%^&*]{8,}$/; 

    function validaCampo(input, regex, spanId, messaggioErrore) {
        const span = document.getElementById(spanId);
        if (!span) return false;

        if (!regex.test(input.value.trim())) {
            input.style.borderColor = '#e74c3c';
            span.textContent = messaggioErrore;
            span.style.display = 'block';
            return false;
        } else {
            input.style.borderColor = '#2ecc71';
            span.style.display = 'none';
            return true;
        }
    }

    inputNome.addEventListener('change', () => validaCampo(inputNome, regexTesto, 'err-nome', 'Nome non valido. Inserisci almeno 2 lettere.'));
    inputCognome.addEventListener('change', () => validaCampo(inputCognome, regexTesto, 'err-cognome', 'Cognome non valido. Inserisci almeno 2 lettere.'));
    inputEmail.addEventListener('change', () => validaCampo(inputEmail, regexEmail, 'err-email', 'Formato email non valido (es. info@zampastico.it).'));
    inputPassword.addEventListener('change', () => validaCampo(inputPassword, regexPassword, 'err-password', 'La password deve avere almeno 8 caratteri, una lettera e un numero.'));

    form.addEventListener('submit', function(event) {
        let isNomeValido = validaCampo(inputNome, regexTesto, 'err-nome', 'Il nome è richiesto.');
        let isCognomeValido = validaCampo(inputCognome, regexTesto, 'err-cognome', 'Il cognome è richiesto.');
        let isEmailValida = validaCampo(inputEmail, regexEmail, 'err-email', 'L\'email è richiesta.');
        let isPasswordValida = validaCampo(inputPassword, regexPassword, 'err-password', 'La password è richiesta.');

        if (!isNomeValido || !isCognomeValido || !isEmailValida || !isPasswordValida) {
            event.preventDefault(); 
        }
    });
});

/* --- PAGINA CHECKOUT --- */
document.addEventListener('DOMContentLoaded', function() {
    const checkoutContainer = document.getElementById('checkout-items-container');
    
    if (checkoutContainer) {
        let carrello = JSON.parse(localStorage.getItem('zampastico_cart')) || [];
        let html = '';
        let totale = 0;

        if (carrello.length === 0) {
            checkoutContainer.innerHTML = `
                <div class="checkout-empty-state">
                    <i class="fa-solid fa-basket-shopping"></i>
                    <p>Il tuo carrello è vuoto!</p>
                </div>`;
            document.getElementById('checkout-total-price').innerText = "0,00€";
            
            const btnPaga = document.querySelector('#formCheckout button[type="submit"]');
            if (btnPaga) {
                btnPaga.disabled = true;
                btnPaga.classList.add('btn-disabled');
            }
            return;
        }

        carrello.forEach(item => {
            let subtotale = item.prezzo * item.quantita;
            totale += subtotale;
            
            html += `
            <div class="checkout-item">
                <div class="checkout-item-details">
                    <span class="item-name">${item.nome}</span>
                    <span class="item-qty">Q.tà: ${item.quantita}</span>
                </div>
                <div class="checkout-item-price">
                    ${subtotale.toFixed(2).replace('.', ',')}€
                </div>
            </div>`;
        });

        checkoutContainer.innerHTML = html;
        document.getElementById('checkout-total-price').innerText = totale.toFixed(2).replace('.', ',') + "€";
    }

    const formCheckout = document.getElementById('formCheckout');
    if (formCheckout) {
        formCheckout.addEventListener('submit', function(e) {
            e.preventDefault(); 
            
            const cap = document.getElementById('cap').value;
            if(!/^\d{5}$/.test(cap)) {
                const inputCap = document.getElementById('cap');
                const errCap = document.getElementById('err-cap');
                
                inputCap.style.borderColor = '#e74c3c';
                errCap.textContent = "Il CAP deve contenere 5 numeri.";
                errCap.style.display = 'block';
                return;
            }

            mostraToast("Ordine elaborato con successo! Grazie per l'acquisto.");
            
            localStorage.removeItem('zampastico_cart');
            if(typeof aggiornaPallinoCarrello === 'function') aggiornaPallinoCarrello();
            
            setTimeout(() => {
                window.location.href = 'catalog'; 
            }, 3000);
        });
    }
});

/* --- SISTEMA DI NOTIFICHE TOAST (Niente più Alert!) --- */
function mostraToast(messaggio) {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = 'toast-msg';
    toast.innerHTML = `<i class="fa-solid fa-check"></i> ${messaggio}`;

    container.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 3500);
}

const btnApplyCoupon = document.getElementById('btn-apply-coupon');
let scontoApplicato = 0; // Memorizza la percentuale di sconto

if (btnApplyCoupon) {
    btnApplyCoupon.addEventListener('click', function() {
        const inputCoupon = document.getElementById('couponCode');
        const couponMsg = document.getElementById('coupon-msg');
        const codice = inputCoupon.value.trim().toUpperCase();
        
        // Mock di codici sconto (In un'app vera questo va chiesto al Backend via Fetch/AJAX!)
        const codiciValidi = {
            'ZAMPA10': 0.10, // 10% di sconto
            'WELCOME20': 0.20 // 20% di sconto
        };

        if (codice === '') {
            mostraMessaggioCoupon('Inserisci un codice', 'error');
            return;
        }

        if (codiciValidi[codice]) {
            scontoApplicato = codiciValidi[codice];
            mostraMessaggioCoupon('Codice applicato con successo!', 'success');
            mostraToast("Sconto " + (scontoApplicato*100) + "% applicato! 💸");
            inputCoupon.disabled = true; // Disabilita l'input dopo l'uso
            this.disabled = true;
            
            // Ricalcola il totale
            ricalcolaTotaleConSconto();
        } else {
            mostraMessaggioCoupon('Codice non valido o scaduto', 'error');
            scontoApplicato = 0;
            ricalcolaTotaleConSconto(); // Resetta se mettono un codice errato
        }
    });
}

function mostraMessaggioCoupon(msg, type) {
    const couponMsg = document.getElementById('coupon-msg');
    couponMsg.textContent = msg;
    couponMsg.className = 'coupon-message ' + (type === 'success' ? 'coupon-success' : 'coupon-error');
    couponMsg.style.display = 'block';
}

function ricalcolaTotaleConSconto() {
    let carrello = JSON.parse(localStorage.getItem('zampastico_cart')) || [];
    let totaleOriginale = 0;

    carrello.forEach(item => {
        totaleOriginale += (item.prezzo * item.quantita);
    });

    let totaleScontato = totaleOriginale - (totaleOriginale * scontoApplicato);
    let scontoValore = totaleOriginale * scontoApplicato;

    // Aggiorna il testo del totale nel DOM
    const totalElement = document.getElementById('checkout-total-price');
    if (totalElement) {
        totalElement.innerHTML = `
            ${scontoApplicato > 0 ? `<span style="font-size: 1rem; color: #777; text-decoration: line-through; margin-right: 10px;">${totaleOriginale.toFixed(2).replace('.', ',')}€</span>` : ''}
            ${totaleScontato.toFixed(2).replace('.', ',')}€
        `;
    }
}