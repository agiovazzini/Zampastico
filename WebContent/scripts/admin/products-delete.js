import { fetchAjax } from './ajax.js';
import { showFeedback } from './ui.js';

export function initProductsDeleteLogic() {
    const delCatSelect = document.getElementById('del-cat-select');
    const delProdSelect = document.getElementById('del-prod-select');
    const delVarSelect = document.getElementById('del-var-select');
    const delWorkspace = document.getElementById('delete-workspace');
    const actionBtns = document.getElementById('action-buttons-container');
    const confirmBox = document.getElementById('confirm-delete-container');
    const targetDesc = document.getElementById('target-desc');
    const btnToggle = document.getElementById('btn-toggle-status');
    const btnDelete = document.getElementById('btn-hard-delete');
    let currentSelection = { type: null, id: null, isActive: false };

    function showDeleteWorkspace(type, id, name, isActive, hasChildren) {
        currentSelection = { type: type, id: id, isActive: isActive };
        if (delWorkspace) delWorkspace.style.display = 'block';
        const targetNameEl = document.getElementById('target-name');
        if (targetNameEl) targetNameEl.innerText = `${name} (${type.toUpperCase()})`;
        if (actionBtns) actionBtns.style.display = 'block';
        if (confirmBox) confirmBox.style.display = 'none';
        if (btnToggle) {
            if (type === 'categoria') {
                btnToggle.style.display = 'none';
            } else {
                btnToggle.style.display = 'inline-block';
                if (isActive) {
                    btnToggle.innerText = "Disattiva/Nascondi";
                } else {
                    btnToggle.innerText = "Riattiva/Mostra";
                }
            }
        }
        if (btnDelete) {
            btnDelete.disabled = hasChildren;
            btnDelete.classList.toggle('btn-disabled', hasChildren);
        }
        if (targetDesc) {
            if (hasChildren) {
                if (type === 'categoria') {
                    targetDesc.innerHTML = "Questa categoria contiene dei prodotti.<br><b>Sposta o elimina i prodotti</b> prima di poter rimuovere l'intera categoria dal database.";
                } else {
                    targetDesc.innerHTML = "Questo elemento contiene dei sotto-elementi dipendenti.<br><b>Svuotalo prima di procedere</b>, oppure limitati a disattivarne la visibilità.";
                }
            } else {
                if (type === 'categoria') {
                    targetDesc.innerHTML = "La categoria è vuota e può essere <b>eliminata definitivamente</b>.";
                } else {
                    targetDesc.innerHTML = "Può essere eliminato dal database <b>solo se non è mai stato acquistato</b>.<br>Se associato a ordini, limitati a disattivarlo.";
                }
            }
        }
    }
    if (delCatSelect) {
        delCatSelect.addEventListener('change', function() {
            if (delProdSelect) {
                delProdSelect.disabled = true;
            }
            if (delVarSelect) {
                delVarSelect.innerHTML = '<option value="">Scegli Variante</option>';
                delVarSelect.disabled = true;
            }
            if (delWorkspace) delWorkspace.style.display = 'none';
            
            fetchAjax('getProductsByCategory', 'idCategoria=' + this.value, function(data) {
                let hasProducts = data.success && data.prodotti.length > 0;
                
                if (delProdSelect) {
                    delProdSelect.innerHTML = '<option value="" selected disabled>Scegli Prodotto</option>';
                    if (hasProducts) {
                        data.prodotti.forEach(p => {
                            let optionText;
                            if (p.marca) {
                                optionText = `${p.marca} - ${p.nome}`;
                            } else {
                                optionText = p.nome;
                            }
                            delProdSelect.innerHTML += `<option value="${p.id}" data-attivo="${p.attivo}">${optionText}</option>`;
                        });
                        delProdSelect.disabled = false;
                    } else {
                        delProdSelect.innerHTML = '<option value="">Nessun prodotto trovato</option>';
                    }
                }
                if (delCatSelect.selectedOptions[0]) {
                    showDeleteWorkspace('categoria', delCatSelect.value, delCatSelect.selectedOptions[0].text.replace(/&nbsp;|└|■/g, '').trim(), true, hasProducts);
                }
            });
        });
    }

    if (delProdSelect) {
        delProdSelect.addEventListener('change', function() {
            let idProd = this.value;
            if (delVarSelect) {
                delVarSelect.disabled = true;
            }
            fetchAjax('getVariantsByProduct', 'idProdotto=' + idProd, function(data) {
                let hasVariants = data.success && data.varianti.length > 0;
                if (delVarSelect) {
                    delVarSelect.innerHTML = '<option value="" selected disabled>Scegli Variante</option>';
                    if (hasVariants) {
                        data.varianti.forEach(v => {
                            delVarSelect.innerHTML += `<option value="${v.id}" data-attivo="${v.disponibile}">${v.formato} - €${v.prezzo}</option>`;
                        });
                        delVarSelect.disabled = false;
                    } else {
                        delVarSelect.innerHTML = '<option value="">Nessuna variante</option>';
                    }
                }
                if (delProdSelect.selectedOptions[0]) {
                    showDeleteWorkspace('prodotto', idProd, delProdSelect.selectedOptions[0].text, delProdSelect.selectedOptions[0].dataset.attivo === 'true', hasVariants);
                }
            });
        });
    }

    if (delVarSelect) {
        delVarSelect.addEventListener('change', function() {
            if (this.selectedOptions[0]) {
                showDeleteWorkspace('variante', this.value, this.selectedOptions[0].text, this.selectedOptions[0].dataset.attivo === 'true', false);
            }
        });
    }

    document.getElementById('btn-hard-delete')?.addEventListener('click', () => {
        if (actionBtns) actionBtns.style.display = 'none';
        if (confirmBox) confirmBox.style.display = 'block';
    });

    document.getElementById('btn-confirm-no')?.addEventListener('click', () => {
        if (confirmBox) confirmBox.style.display = 'none';
        if (actionBtns) actionBtns.style.display = 'block';
    });

    document.getElementById('btn-confirm-yes')?.addEventListener('click', () => {
        fetchAjax('hardDelete', `type=${currentSelection.type}&id=${currentSelection.id}`, function(res) {
            if (res.success) {
                showFeedback("Elemento rimosso dal database.");
                if (confirmBox) confirmBox.style.display = 'none';
                if (actionBtns) actionBtns.style.display = 'block';
                if (delWorkspace) delWorkspace.style.display = 'none';
                
                if (currentSelection.type === 'variante') {
                    if (delVarSelect) delVarSelect.value = "";
                    if (delProdSelect) delProdSelect.dispatchEvent(new Event('change'));
                } 
                else if (currentSelection.type === 'prodotto') {
                    if (delProdSelect) delProdSelect.value = "";
                    if (delCatSelect) delCatSelect.dispatchEvent(new Event('change'));
                } 
                else if (currentSelection.type === 'categoria') {
                    const categorySelects = [
                        document.getElementById('idCategoria'),
                        document.getElementById('edit-cat-select'),
                        document.getElementById('edit-idCategoria'),
                        document.getElementById('del-cat-select')
                    ];
                    categorySelects.forEach(select => {
                        if (select) {
                            const optionToRemove = select.querySelector(`option[value="${currentSelection.id}"]`);
                            if (optionToRemove) optionToRemove.remove();
                        }
                    });
                    if (delCatSelect) {
                        delCatSelect.value = "";
                        delCatSelect.dispatchEvent(new Event('change'));
                    }
                }
            } else {
                showFeedback(res.message);
                if (confirmBox) confirmBox.style.display = 'none';
                if (actionBtns) actionBtns.style.display = 'block';
            }
        });
    });

    btnToggle?.addEventListener('click', () => {
        let newStatus = !currentSelection.isActive;
        fetchAjax('toggleStatus', `type=${currentSelection.type}&id=${currentSelection.id}&status=${newStatus}`, function(res) {
            if (res.success) {
                showFeedback("Visibilità aggiornata correttamente.");
                currentSelection.isActive = newStatus;
                
                if (btnToggle) {
                    if (newStatus) {
                        btnToggle.innerText = "Disattiva/Nascondi";
                    } else {
                        btnToggle.innerText = "Riattiva/Mostra";
                    }
                }
                
                if (currentSelection.type === 'prodotto' && delProdSelect && delProdSelect.selectedOptions[0]) {
                    delProdSelect.selectedOptions[0].dataset.attivo = newStatus;
                } else if (currentSelection.type === 'variante' && delVarSelect && delVarSelect.selectedOptions[0]) {
                    delVarSelect.selectedOptions[0].dataset.attivo = newStatus;
                }
            } else {
                showFeedback(res.message);
            }
        });
    });
}