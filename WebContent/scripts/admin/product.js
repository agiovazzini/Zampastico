document.addEventListener("DOMContentLoaded", function() {
    
    // ==========================================
    // 0. FEEDBACK GLOBALE UNIFICATO
    // ==========================================
    function showFeedback(message) {
        let feedbackDiv = document.querySelector('.feedback-div');
        
        // Se non esiste, crea il componente e aggancialo sotto l'header principale
        if (!feedbackDiv) {
            feedbackDiv = document.createElement('div');
            feedbackDiv.className = 'feedback-div';
            
            const header = document.querySelector('.admin-products-header');
            if (header) {
                header.insertAdjacentElement('afterend', feedbackDiv);
            }
        }
        
        feedbackDiv.textContent = message;
        feedbackDiv.style.display = 'block';
        window.scrollTo({ top: 0, behavior: 'smooth' });
        
        // Nasconde automaticamente il messaggio dopo 5 secondi
        setTimeout(() => {
            feedbackDiv.style.display = 'none';
        }, 5000);
    }
	
    // ==========================================
    // 1. GESTIONE CREAZIONE NUOVA CATEGORIA AL VOLO
    // ==========================================
    const idCategoriaSelect = document.getElementById('idCategoria');
    const newCatContainer = document.getElementById('new-cat-container');
    const nuovaCategoriaInput = document.getElementById('nuovaCategoria');
    const nuovaSupercategoriaInput = document.getElementById('nuovaSupercategoria');

    if (idCategoriaSelect && newCatContainer && nuovaCategoriaInput && nuovaSupercategoriaInput) {
        idCategoriaSelect.addEventListener('change', function() {
            if (this.value === 'new') {
                newCatContainer.style.display = 'flex'; 
                nuovaCategoriaInput.required = true;       // Obbligatorio
                nuovaSupercategoriaInput.required = false; // SEMPRE Opzionale
                nuovaCategoriaInput.focus();
            } else {
                newCatContainer.style.display = 'none';
                nuovaCategoriaInput.required = false;
                nuovaSupercategoriaInput.required = false;
                nuovaCategoriaInput.value = '';
                nuovaSupercategoriaInput.value = '';
            }
        });
    }

    // ==========================================
    // 2. GESTIONE NAVIGAZIONE TABS PRINCIPALI
    // ==========================================
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.product-tab-content');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));
            btn.classList.add('active');
            document.getElementById(btn.dataset.target).classList.add('active');
            
            // Nasconde eventuali feedback precedenti quando si cambia pannello
            const feedback = document.querySelector('.feedback-div');
            if (feedback) feedback.style.display = 'none';
        });
    });

    // ==========================================
    // 3. FUNZIONE GENERICA CHIAMATE AJAX
    // ==========================================
    function fetchAjax(action, params, callback) {
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "manageProducts", true);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                try {
                    callback(JSON.parse(xhr.responseText));
                } catch (e) {
                    showFeedback("Errore funzionalità AJAX.");
                }
            }
        };
        xhr.send(`action=${action}&${params}`);
    }

    // ==========================================
    // 4. LOGICA TAB MODIFICA PRODOTTI E VARIANTI
    // ==========================================
    const editCatSelect = document.getElementById('edit-cat-select');
    const editProdSelect = document.getElementById('edit-prod-select');
    const editVarSelect = document.getElementById('edit-var-select');
    const placeholder = document.getElementById('edit-placeholder');
    const formUpdateProduct = document.getElementById('form-update-product');
    const formUpdateVariant = document.getElementById('form-update-variant');
    const formCreateVariant = document.getElementById('form-create-variant');

    function hideAllEditForms() {
        placeholder.style.display = 'none';
        formUpdateProduct.style.display = 'none';
        formUpdateVariant.style.display = 'none';
        formCreateVariant.style.display = 'none';
    }

    if (editCatSelect) {
        editCatSelect.addEventListener('change', function() {
            hideAllEditForms();
            placeholder.style.display = 'block';
            editProdSelect.innerHTML = '<option value="">Caricamento...</option>';
            editProdSelect.disabled = true;
            editVarSelect.innerHTML = '<option value="">Scegli Variante</option>'; 
            editVarSelect.disabled = true;
            
            fetchAjax('getProductsByCategory', 'idCategoria=' + this.value, function(data) {
                if(data.success && data.prodotti.length > 0) {
                    editProdSelect.innerHTML = '<option value="" selected disabled>2. Scegli Prodotto</option>';
                    data.prodotti.forEach(p => {
                        let optionText = p.marca ? `#${p.marca} - ${p.nome}` : p.nome;
                        editProdSelect.innerHTML += `<option value="${p.id}" data-marca="${p.marca}">${optionText}</option>`;
                    });
                    editProdSelect.disabled = false;
                } else {
                    editProdSelect.innerHTML = '<option value="">Nessun prodotto trovato</option>';
                }
            });
        });
    }

    if (editProdSelect) {
        editProdSelect.addEventListener('change', function() {
            let idProd = this.value;
            fetchAjax('getVariantsByProduct', 'idProdotto=' + idProd, function(data) {
                editVarSelect.innerHTML = '<option value="" selected disabled>3. Scegli Variante (Opzionale)</option>';
                if(data.success && data.varianti.length > 0) {
                    data.varianti.forEach(v => {
                        editVarSelect.innerHTML += `<option value="${v.id}">${v.formato} - €${v.prezzo}</option>`;
                    });
                    editVarSelect.disabled = false;
                } else {
                    editVarSelect.innerHTML = '<option value="">Nessuna variante</option>';
                }
            });
            
            fetchAjax('getProductDetails', 'idProdotto=' + idProd, function(data) {
                if (data.success) {
                    hideAllEditForms();
                    document.getElementById('edit-hidden-idProd').value = data.id;
                    document.getElementById('edit-idCategoria').value = data.idCategoria;
                    document.getElementById('edit-nome').value = data.nome;
                    document.getElementById('edit-marca').value = data.marca;
                    document.getElementById('edit-descrizione').value = data.descrizione;
                    document.getElementById('edit-prodottoAttivo').checked = data.attivo;
                    formUpdateProduct.style.display = 'block';
                }
            });
        });
    }

    if (editVarSelect) {
        editVarSelect.addEventListener('change', function() {
            if(!this.value) return;
            fetchAjax('getVariantDetails', 'idVariante=' + this.value, function(data) {
                if (data.success) {
                    hideAllEditForms();
                    document.getElementById('edit-hidden-idVar').value = data.id;
                    document.getElementById('edit-formato').value = data.formato;
                    document.getElementById('edit-prezzo').value = data.prezzo;
                    document.getElementById('edit-varianteDisponibile').checked = data.disponibile;
                    formUpdateVariant.style.display = 'block';
                }
            });
        });
    }

    // Event Listeners Bottoni Interfaccia Modifica
    document.getElementById('btn-show-add-variant')?.addEventListener('click', function() {
        hideAllEditForms();
        document.getElementById('new-formato').value = '';
        document.getElementById('new-prezzo').value = '';
        document.getElementById('new-varianteDisponibile').checked = true;
        formCreateVariant.style.display = 'block';
    });

    document.getElementById('btn-cancel-create-var')?.addEventListener('click', function() {
        hideAllEditForms();
        formUpdateProduct.style.display = 'block';
    });

    document.getElementById('btn-back-from-edit-var')?.addEventListener('click', function() {
        editVarSelect.value = ""; 
        hideAllEditForms();
        formUpdateProduct.style.display = 'block';
    });

    // Submit Modifiche Prodotto
    formUpdateProduct?.addEventListener('submit', function(e) {
        e.preventDefault();
        let idProd = document.getElementById('edit-hidden-idProd').value;
        let nome = document.getElementById('edit-nome').value;
        let marca = document.getElementById('edit-marca').value;
        let desc = document.getElementById('edit-descrizione').value;
        let idCat = document.getElementById('edit-idCategoria').value;
        let att = document.getElementById('edit-prodottoAttivo').checked;

        let params = `idProdotto=${idProd}&nome=${encodeURIComponent(nome)}&marca=${encodeURIComponent(marca)}&descrizione=${encodeURIComponent(desc)}&idCategoria=${idCat}`;
        if (att) params += '&prodottoAttivo=true';

        fetchAjax('updateProduct', params, function(res) {
            if (res.success) {
                showFeedback("Prodotto aggiornato con successo nel database!");
                editProdSelect.selectedOptions[0].text = marca ? `#${marca} - ${nome}` : nome;
            } else {
                showFeedback(res.message);
            }
        });
    });

    // Submit Modifiche Variante
    formUpdateVariant?.addEventListener('submit', function(e) {
        e.preventDefault();
        let idVar = document.getElementById('edit-hidden-idVar').value;
        let formato = document.getElementById('edit-formato').value;
        let prezzo = document.getElementById('edit-prezzo').value;
        let disp = document.getElementById('edit-varianteDisponibile').checked;

        let params = `idVariante=${idVar}&formato=${encodeURIComponent(formato)}&prezzo=${prezzo}`;
        if (disp) params += '&varianteDisponibile=true';

        fetchAjax('updateVariant', params, function(res) {
            if (res.success) {
                showFeedback("Variante aggiornata con successo!");
                editVarSelect.selectedOptions[0].text = `${formato} - €${prezzo}`;
            } else {
                showFeedback(res.message);
            }
        });
    });

    // Submit Creazione Nuova Variante
    formCreateVariant?.addEventListener('submit', function(e) {
        e.preventDefault();
        let idProd = document.getElementById('edit-hidden-idProd').value;
        let formato = document.getElementById('new-formato').value;
        let prezzo = document.getElementById('new-prezzo').value;
        let disp = document.getElementById('new-varianteDisponibile').checked;

        let params = `idProdotto=${idProd}&formato=${encodeURIComponent(formato)}&prezzo=${prezzo}`;
        if (disp) params += '&varianteDisponibile=true';

        fetchAjax('createVariant', params, function(res) {
            showFeedback(res.message);
            if (res.success) {
                editProdSelect.dispatchEvent(new Event('change')); // Ricarica la lista per mostrare la nuova variante
            }
        });
    });

    // ==========================================
    // 5. LOGICA TAB ELIMINAZIONE / DISATTIVAZIONE
    // ==========================================
    const delCatSelect = document.getElementById('del-cat-select');
    const delProdSelect = document.getElementById('del-prod-select');
    const delVarSelect = document.getElementById('del-var-select');
    const delWorkspace = document.getElementById('delete-workspace');
    
    // Elementi UI Eliminazione
    const actionBtns = document.getElementById('action-buttons-container');
    const confirmBox = document.getElementById('confirm-delete-container');
    const targetDesc = document.getElementById('target-desc');
    const btnToggle = document.getElementById('btn-toggle-status');
    const btnDelete = document.getElementById('btn-hard-delete');

    let currentSelection = { type: null, id: null, isActive: false };

    if (delCatSelect) {
        delCatSelect.addEventListener('change', function() {
            delProdSelect.innerHTML = '<option value="">Caricamento...</option>';
            delProdSelect.disabled = true;
            delVarSelect.innerHTML = '<option value="">3. Scegli Variante (Opzionale)</option>';
            delVarSelect.disabled = true;
            delWorkspace.style.display = 'none';

            fetchAjax('getProductsByCategory', 'idCategoria=' + this.value, function(data) {
                delProdSelect.innerHTML = '<option value="" selected disabled>2. Scegli Prodotto</option>';
                
                let hasProducts = data.success && data.prodotti.length > 0;
                
                if(hasProducts) {
                    data.prodotti.forEach(p => {
                        let optionText = p.marca ? `#${p.marca} - ${p.nome}` : p.nome;
                        delProdSelect.innerHTML += `<option value="${p.id}" data-attivo="${p.attivo}">${optionText}</option>`;
                    });
                    delProdSelect.disabled = false;
                } else {
                    delProdSelect.innerHTML = '<option value="">Nessun prodotto trovato</option>';
                }
                
                // Mostra pannello anche se la categoria è vuota
                showDeleteWorkspace('categoria', delCatSelect.value, delCatSelect.selectedOptions[0].text.replace(/&nbsp;|└|■/g, '').trim(), true, hasProducts);
            });
        });
    }

    if (delProdSelect) {
        delProdSelect.addEventListener('change', function() {
            let idProd = this.value;
            delVarSelect.innerHTML = '<option value="">Caricamento...</option>';
            delVarSelect.disabled = true;

            fetchAjax('getVariantsByProduct', 'idProdotto=' + idProd, function(data) {
                delVarSelect.innerHTML = '<option value="" selected disabled>3. Scegli Variante (Opzionale)</option>';
                let hasVariants = data.success && data.varianti.length > 0;
                
                if(hasVariants) {
                    data.varianti.forEach(v => {
                        delVarSelect.innerHTML += `<option value="${v.id}" data-attivo="${v.disponibile}">${v.formato} - €${v.prezzo}</option>`;
                    });
                    delVarSelect.disabled = false;
                } else {
                    delVarSelect.innerHTML = '<option value="">Nessuna variante</option>';
                }
                showDeleteWorkspace('prodotto', idProd, delProdSelect.selectedOptions[0].text, delProdSelect.selectedOptions[0].dataset.attivo === 'true', hasVariants);
            });
        });
    }

    if (delVarSelect) {
        delVarSelect.addEventListener('change', function() {
            showDeleteWorkspace('variante', this.value, this.selectedOptions[0].text, this.selectedOptions[0].dataset.attivo === 'true', false);
        });
    }

    function showDeleteWorkspace(type, id, name, isActive, hasChildren) {
        currentSelection = { type: type, id: id, isActive: isActive };
        delWorkspace.style.display = 'block';
        document.getElementById('target-name').innerText = `${name} (${type.toUpperCase()})`;
        
        actionBtns.style.display = 'flex';
        confirmBox.style.display = 'none';
        
        // Disabilita lo stato Toggle per le Categorie (non hanno colonna "attivo")
        if (type === 'categoria') {
            btnToggle.style.display = 'none';
        } else {
            btnToggle.style.display = 'block';
            btnToggle.innerText = isActive ? "Disattiva/Nascondi" : "Riattiva/Mostra";
        }
        
        btnDelete.disabled = hasChildren;
        btnDelete.classList.toggle('btn-disabled', hasChildren);
        
        if (hasChildren) {
            targetDesc.innerHTML = type === 'categoria' 
                ? "Questa categoria contiene dei prodotti.<br><b>Sposta o elimina i prodotti</b> prima di poter rimuovere l'intera categoria dal database."
                : "Questo elemento contiene dei sotto-elementi dipendenti.<br><b>Svuotalo prima di procedere</b>, oppure limitati a disattivarne la visibilità.";
        } else {
            targetDesc.innerHTML = type === 'categoria'
                ? "La categoria è vuota e può essere <b>eliminata definitivamente</b>."
                : "Può essere eliminato dal database <b>solo se non è mai stato acquistato</b>.<br>Se associato a ordini, limitati a disattivarlo.";
        }
    }

    document.getElementById('btn-hard-delete')?.addEventListener('click', () => {
        actionBtns.style.display = 'none';
        confirmBox.style.display = 'block';
    });

    document.getElementById('btn-confirm-no')?.addEventListener('click', () => {
        confirmBox.style.display = 'none';
        actionBtns.style.display = 'flex';
    });

    // Conferma Eliminazione Definitiva
    document.getElementById('btn-confirm-yes')?.addEventListener('click', () => {
        fetchAjax('hardDelete', `type=${currentSelection.type}&id=${currentSelection.id}`, function(res) {
            if(res.success) {
                showFeedback("Elemento rimosso dal database.");
                
                confirmBox.style.display = 'none';
                actionBtns.style.display = 'flex';
                delWorkspace.style.display = 'none';

                // Aggiornamento DOM senza ricaricare la pagina
                if (currentSelection.type === 'variante') {
                    delVarSelect.value = "";
                    delProdSelect.dispatchEvent(new Event('change'));
                } 
                else if (currentSelection.type === 'prodotto') {
                    delProdSelect.value = "";
                    delCatSelect.dispatchEvent(new Event('change'));
                } 
                else if (currentSelection.type === 'categoria') {
                    // Cerca l'opzione in TUTTE le select e la rimuove
                    const categorySelects = [
                        document.getElementById('idCategoria'),
                        document.getElementById('edit-cat-select'),
                        document.getElementById('edit-idCategoria'),
                        document.getElementById('del-cat-select')
                    ];
                    
                    categorySelects.forEach(select => {
                        if (select) {
                            const optionToRemove = select.querySelector(`option[value="${currentSelection.id}"]`);
                            if (optionToRemove) {
                                optionToRemove.remove();
                            }
                        }
                    });

                    // Resetta la select attuale
                    delCatSelect.value = "";
                    delCatSelect.dispatchEvent(new Event('change'));
                }
            } else {
                showFeedback(res.message);
                confirmBox.style.display = 'none';
                actionBtns.style.display = 'flex';
            }
        });
    });

    // Cambio Stato (Attivo / Inattivo)
    btnToggle?.addEventListener('click', () => {
        let newStatus = !currentSelection.isActive;
        fetchAjax('toggleStatus', `type=${currentSelection.type}&id=${currentSelection.id}&status=${newStatus}`, function(res) {
            if(res.success) {
                showFeedback("Visibilità aggiornata correttamente.");
                currentSelection.isActive = newStatus;
                btnToggle.innerText = newStatus ? "Disattiva/Nascondi" : "Riattiva/Mostra";
                
                if(currentSelection.type === 'prodotto') {
                    delProdSelect.selectedOptions[0].dataset.attivo = newStatus;
                } else if(currentSelection.type === 'variante') {
                    delVarSelect.selectedOptions[0].dataset.attivo = newStatus;
                }
            } else {
                showFeedback(res.message);
            }
        });
    });
});