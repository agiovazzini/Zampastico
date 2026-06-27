import { fetchAjax } from './ajax.js';
import { showFeedback } from './ui.js';

export function initProductsEditLogic() {
    const editCatSelect = document.getElementById('edit-cat-select');
    const editProdSelect = document.getElementById('edit-prod-select');
    const editVarSelect = document.getElementById('edit-var-select');
    const placeholder = document.getElementById('edit-placeholder');
    const formUpdateProduct = document.getElementById('form-update-product');
    const formUpdateVariant = document.getElementById('form-update-variant');
    const formCreateVariant = document.getElementById('form-create-variant');

    function hideAllEditForms() {
        if (placeholder) placeholder.style.display = 'none';
        if (formUpdateProduct) formUpdateProduct.style.display = 'none';
        if (formUpdateVariant) formUpdateVariant.style.display = 'none';
        if (formCreateVariant) formCreateVariant.style.display = 'none';
    }

    if (editCatSelect) {
        editCatSelect.addEventListener('change', function() {
            hideAllEditForms();
            if (placeholder) placeholder.style.display = 'block';
            if (editVarSelect) {
                editVarSelect.innerHTML = '<option value="">Scegli Variante</option>'; 
                editVarSelect.disabled = true;
            }
            fetchAjax('getProductsByCategory', 'idCategoria=' + this.value, function(data) {
                if (editProdSelect) {
                    if (data.success && data.prodotti.length > 0) {
                        editProdSelect.innerHTML = '<option value="" selected>Scegli Prodotto</option>';
                        data.prodotti.forEach(p => {
                            let optionText = p.marca ? `#${p.marca} - ${p.nome}` : p.nome;
                            editProdSelect.innerHTML += `<option value="${p.id}" data-marca="${p.marca}">${optionText}</option>`;
                        });
                        editProdSelect.disabled = false;
                    } else {
                        editProdSelect.innerHTML = '<option value="">Nessun prodotto trovato</option>';
                    }
                }
            });
        });
    }

    if (editProdSelect) {
        editProdSelect.addEventListener('change', function() {
            let idProd = this.value;
            fetchAjax('getVariantsByProduct', 'idProdotto=' + idProd, function(data) {
                if (editVarSelect) {
                    editVarSelect.innerHTML = '<option value="" selected disabled>Scegli Variante</option>';
                    if (data.success && data.varianti.length > 0) {
                        data.varianti.forEach(v => {
                            editVarSelect.innerHTML += `<option value="${v.id}">${v.formato} - &euro;${v.prezzo}</option>`;
                        });
                        editVarSelect.disabled = false;
                    } else {
                        editVarSelect.innerHTML = '<option value="">Nessuna variante</option>';
                    }
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
                    let imgCurrent = document.getElementById('edit-img-current');
                    let wrapperCurrent = document.getElementById('wrapper-edit-img-current');
                    let wrapperNew = document.getElementById('wrapper-edit-img-new');
                    let fileInput = document.getElementById('edit-immagineProdotto');
                    let flagRemove = document.getElementById('remove-img-prod-flag');
                    if (fileInput) fileInput.value = ""; 
                    if (flagRemove) flagRemove.value = "false";
                    if (wrapperNew) wrapperNew.style.display = "none";
                    
                    if (wrapperCurrent && imgCurrent) {
                        if (data.path) {
                            imgCurrent.src = "../image?action=show&code=" + data.id + "&t=" + new Date().getTime();
                            wrapperCurrent.style.display = "block";
                        } else {
                            imgCurrent.src = "";
                            wrapperCurrent.style.display = "none";
                        }
                    }
                    
                    if (formUpdateProduct) formUpdateProduct.style.display = 'block';
                }
            });
        });
    }

    if (editVarSelect) {
        editVarSelect.addEventListener('change', function() {
            if (!this.value) return;
            fetchAjax('getVariantDetails', 'idVariante=' + this.value, function(data) {
                if (data.success) {
                    hideAllEditForms();
                    
                    document.getElementById('edit-hidden-idVar').value = data.id;
                    document.getElementById('edit-formato').value = data.formato;
                    document.getElementById('edit-prezzo').value = data.prezzo;
                    document.getElementById('edit-varianteDisponibile').checked = data.disponibile;
                    let imgCurrent = document.getElementById('edit-var-img-current');
                    let wrapperCurrent = document.getElementById('wrapper-edit-var-img-current');
                    let wrapperNew = document.getElementById('wrapper-edit-var-img-new');
                    let fileInput = document.getElementById('edit-immagineVariante');
                    let flagRemove = document.getElementById('remove-img-var-flag');
                    if (fileInput) fileInput.value = "";
                    if (flagRemove) flagRemove.value = "false";
                    if (wrapperNew) wrapperNew.style.display = "none";
                    if (wrapperCurrent && imgCurrent) {
                        if (data.path) {
                            imgCurrent.src = "../image?action=showVar&code=" + data.id + "&t=" + new Date().getTime();
                            wrapperCurrent.style.display = "block";
                        } else {
                            imgCurrent.src = "";
                            wrapperCurrent.style.display = "none";
                        }
                    }
                    if (formUpdateVariant) formUpdateVariant.style.display = 'block';
                }
            });
        });
    }

    document.getElementById('btn-remove-edit-img-prod')?.addEventListener('click', function() {
        const wrapper = document.getElementById('wrapper-edit-img-current');
        const flag = document.getElementById('remove-img-prod-flag');
        if (wrapper) wrapper.style.display = 'none';
        if (flag) flag.value = "true";
    });

    document.getElementById('btn-remove-edit-img-var')?.addEventListener('click', function() {
        const wrapper = document.getElementById('wrapper-edit-var-img-current');
        const flag = document.getElementById('remove-img-var-flag');
        if (wrapper) wrapper.style.display = 'none';
        if (flag) flag.value = "true";
    });

    document.getElementById('btn-show-add-variant')?.addEventListener('click', function() {
        hideAllEditForms();
        document.getElementById('new-formato').value = '';
        document.getElementById('new-prezzo').value = '';
        document.getElementById('new-varianteDisponibile').checked = true;
        const previewWrapper = document.getElementById('wrapper-new-var-img-preview');
        if (previewWrapper) previewWrapper.style.display = 'none';
        const fileInput = document.getElementById('new-immagineVariante');
        if (fileInput) fileInput.value = "";
        if (formCreateVariant) formCreateVariant.style.display = 'block';
    });

    document.getElementById('btn-cancel-create-var')?.addEventListener('click', function() {
        hideAllEditForms();
        if (formUpdateProduct) formUpdateProduct.style.display = 'block';
    });

    document.getElementById('btn-back-from-edit-var')?.addEventListener('click', function() {
        if (editVarSelect) editVarSelect.value = ""; 
        hideAllEditForms();
        if (formUpdateProduct) formUpdateProduct.style.display = 'block';
    });

    formUpdateProduct?.addEventListener('submit', function(e) {
        e.preventDefault();
        let formData = new FormData();
        formData.append('idProdotto', document.getElementById('edit-hidden-idProd').value);
        formData.append('nome', document.getElementById('edit-nome').value);
        formData.append('marca', document.getElementById('edit-marca').value);
        formData.append('descrizione', document.getElementById('edit-descrizione').value);
        formData.append('idCategoria', document.getElementById('edit-idCategoria').value);
        if (document.getElementById('edit-prodottoAttivo').checked) {
            formData.append('prodottoAttivo', 'true');
        }
        let removeFlag = document.getElementById('remove-img-prod-flag');
        if (removeFlag) formData.append('removeImmagineProdotto', removeFlag.value);
        let imgInput = document.getElementById('edit-immagineProdotto');
        if (imgInput && imgInput.files.length > 0) {
            formData.append('immagineProdotto', imgInput.files[0]);
        }
        
        fetchAjax('updateProduct', formData, function(res) {
            if (res.success) {
                showFeedback("Prodotto aggiornato con successo nel database!");
                let marca = document.getElementById('edit-marca').value;
                let nome = document.getElementById('edit-nome').value;
                if (editProdSelect && editProdSelect.selectedOptions[0]) {
                    editProdSelect.selectedOptions[0].text = marca ? `#${marca} - ${nome}` : nome;
                    editProdSelect.dispatchEvent(new Event('change'));
                }
            } else {
                showFeedback(res.message);
            }
        });
    });

    formUpdateVariant?.addEventListener('submit', function(e) {
        e.preventDefault();
        let formData = new FormData();
        formData.append('idVariante', document.getElementById('edit-hidden-idVar').value);
        formData.append('formato', document.getElementById('edit-formato').value);
        formData.append('prezzo', document.getElementById('edit-prezzo').value);
        if (document.getElementById('edit-varianteDisponibile').checked) {
            formData.append('varianteDisponibile', 'true');
        }
        let removeFlag = document.getElementById('remove-img-var-flag');
        if (removeFlag) formData.append('removeImmagineVariante', removeFlag.value);
        let imgInput = document.getElementById('edit-immagineVariante');
        if (imgInput && imgInput.files.length > 0) {
            formData.append('immagineVariante', imgInput.files[0]);
        }
        fetchAjax('updateVariant', formData, function(res) {
            if (res.success) {
                showFeedback("Variante aggiornata con successo!");
                let formato = document.getElementById('edit-formato').value;
                let prezzo = document.getElementById('edit-prezzo').value;
                if (editVarSelect && editVarSelect.selectedOptions[0]) {
                    editVarSelect.selectedOptions[0].text = `${formato} - €${prezzo}`;
                    editVarSelect.dispatchEvent(new Event('change'));
                }
            } else {
                showFeedback(res.message);
            }
        });
    });

    formCreateVariant?.addEventListener('submit', function(e) {
        e.preventDefault();
        let formData = new FormData();
        formData.append('idProdotto', document.getElementById('edit-hidden-idProd').value);
        formData.append('formato', document.getElementById('new-formato').value);
        formData.append('prezzo', document.getElementById('new-prezzo').value);
        if (document.getElementById('new-varianteDisponibile').checked) {
            formData.append('varianteDisponibile', 'true');
        }
        let imgInput = document.getElementById('new-immagineVariante');
        if (imgInput && imgInput.files.length > 0) {
            formData.append('immagineVariante', imgInput.files[0]);
        }
        fetchAjax('createVariant', formData, function(res) {
            showFeedback(res.message);
            if (res.success) {
                if (imgInput) imgInput.value = ""; 
                if (editProdSelect) editProdSelect.dispatchEvent(new Event('change'));
            }
        });
    });
}