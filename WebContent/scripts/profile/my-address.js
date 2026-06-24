function showFeedback(message) {
	let feedbackDiv = document.querySelector('.feedback-div');
	if (!feedbackDiv) {
		feedbackDiv = document.createElement('div');
		feedbackDiv.className = 'feedback-div';
		const tabContent = document.querySelector('.tab-content');
		if (tabContent) {
			tabContent.prepend(feedbackDiv);
		}
	}
	feedbackDiv.textContent = message;
	feedbackDiv.style.display = 'block';
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function hideFeedback() {
	const feedbackDiv = document.querySelector('.feedback-div');
	if (feedbackDiv) {
		feedbackDiv.style.display = 'none';
		feedbackDiv.textContent = '';
	}
}

function submitAddress() {
    hideFeedback();
    const form = document.getElementById('address-settings');
    const street = document.getElementById('addressStreet').value.trim();
    const houseNumber = document.getElementById('addressHouseNumber').value.trim();
    const city = document.getElementById('addressCity').value.trim();
    const province = document.getElementById('addressProvince').value.trim();
    const postCode = document.getElementById('addressPostCode').value.trim();
    const capRegex = /^\d{5}$/;
    const provinceRegex = /^[a-zA-Z]{2}$/;
    if (street === "" || houseNumber === "" || city === "" || province === "" || postCode === "") {
        showFeedback("Tutti i campi dell'indirizzo sono obbligatori.");
        return;
    }
    if (!capRegex.test(postCode)) {
        showFeedback("Formato CAP non valido. Deve essere composto da esattamente 5 cifre.");
        return;
    }
    if (!provinceRegex.test(province)) {
        showFeedback("Formato Provincia non valido. Inserisci la sigla di 2 lettere (es. RM, NA, MI).");
        return;
    }
    document.getElementById('addressProvince').value = province.toUpperCase();
    form.submit();
}


function editAddress(button) {
    hideFeedback();
	const formContainer = document.querySelector('.address-container');
	const form = document.getElementById('address-settings');
	const formTitle = document.querySelector('.address-container h3');
	const submitButton = document.querySelector('.address-form .saveButton');
	const cancelButton = document.getElementById('cancelEditButton');
    const id = button.getAttribute('data-id');
    const viaCompleta = button.getAttribute('data-via'); 
    const citta = button.getAttribute('data-citta');
    const provincia = button.getAttribute('data-provincia');
    const cap = button.getAttribute('data-cap');
    const isDefault = button.getAttribute('data-default') === 'true';
    const lastCommaIndex = viaCompleta.lastIndexOf(',');
    let via = viaCompleta;
    let civico = "";
    if (lastCommaIndex !== -1) {
        via = viaCompleta.substring(0, lastCommaIndex).trim();
        civico = viaCompleta.substring(lastCommaIndex + 1).trim();
    }
    document.getElementById('idAddress').value = id;
    document.getElementById('addressStreet').value = via;
    document.getElementById('addressHouseNumber').value = civico;
    document.getElementById('addressCity').value = citta;
    document.getElementById('addressProvince').value = provincia;
    document.getElementById('addressPostCode').value = cap;
    document.getElementById('addressDefault').checked = isDefault;
    if (formTitle){ 
		formTitle.textContent = "Modifica l'indirizzo salvato";
    }
    if (submitButton){ 
		submitButton.textContent = "Salva modifiche";
	}
	if (cancelButton){
		cancelButton.style.display = "inline-block";	
	}
    form.action = form.action.replace("/addAddress", "/updateAddress");
    document.querySelectorAll('.address-element').forEach(el => el.classList.remove('highlight-card'));
    button.closest('.address-element').classList.add('highlight-card');
		if (formContainer) {
		    formContainer.classList.add('edit-mode');
		    formContainer.classList.remove('locked-mode');
		    const form = document.getElementById('address-settings');
		    const inputs = form.querySelectorAll('input, button');
		    inputs.forEach(input => input.disabled = false);
		}
}

function resetForm() {
    hideFeedback();
    const form = document.getElementById('address-settings');
    form.reset();
    document.getElementById('idAddress').value = "";
	const formContainer = document.querySelector('.address-container');
    const formTitle = document.querySelector('.address-container h3');
    const submitBtn = document.querySelector('.address-form .saveButton');
    const cancelBtn = document.getElementById('cancelEditButton');
    if (formTitle){
		formTitle.textContent = "Aggiungi un nuovo indirizzo";
	}	
    if(submitBtn) {
		submitBtn.textContent = "Salva indirizzo";
	}
    if(cancelBtn) {
		cancelBtn.style.display = "none";
	}
	form.action = form.action.replace("/updateAddress", "/addAddress");
	document.querySelectorAll('.address-element').forEach(el => el.classList.remove('highlight-card'));
	if (formContainer) {
		formContainer.classList.remove('edit-mode');
		const addressCount = document.querySelectorAll('.address-element').length;
		if (addressCount >= 5) {
			formContainer.classList.add('locked-mode');    
			const form = document.getElementById('address-settings');
			const inputs = form.querySelectorAll('input, button');
			inputs.forEach(input => input.disabled = true);
		}
	}
}