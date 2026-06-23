let confirmTimeout;

function showFeedback(message){
	let feedbackDiv = document.querySelector('.feedback-div');
	if (!feedbackDiv){
		feedbackDiv = document.createElement('div');
		feedbackDiv.className = 'feedback-div';
		const tabContent = document.querySelector('.tab-content');
		if (tabContent){
			tabContent.prepend(feedbackDiv);
		}
	}
	feedbackDiv.textContent = message;
	feedbackDiv.style.display = 'block';
}

function hideFeedback(){
	const feedbackDiv = document.querySelector('.feedback-div');
	if (feedbackDiv){
		feedbackDiv.style.display = 'none';
		feedbackDiv.textContent = '';
	}
}

function submitUpdate() {
    hideFeedback();
    const form = document.getElementById('profile-settings');
    const nome = document.getElementById('name').value.trim();
    const cognome = document.getElementById('surname').value.trim();
    const nameRegex = /^[a-zA-ZÀ-ÿ\s']{2,50}$/;
    if (nome === "" || cognome === "") {
        showFeedback("Nome e cognome non possono essere vuoti.");
        return;
    }
    if (!nameRegex.test(nome) || !nameRegex.test(cognome)) {
        showFeedback("Formato non valido. Usa solo lettere, spazi o apostrofi (minimo 2 caratteri).");
        return;
    }
    if (nome.toLowerCase() === cognome.toLowerCase()) {
        showFeedback("Il nome e il cognome non possono essere identici.");
        return;
    }
    form.submit();
}

function submitPassword() {
    hideFeedback();
	const form = document.getElementById('password-settings');
	const oldPass = document.getElementById('currentPassword').value.trim();
	const newPass = document.getElementById('newPassword').value.trim();
	const confPass = document.getElementById('confirmPassword').value.trim();
	const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.\-_#'])[A-Za-z\d@$!%*?&.\-_#']{8,}$/;
	if (oldPass === "" || newPass === "" || confPass === "") {
		showFeedback("Compila tutti i campi della password.");
		return;
	}
	if (oldPass === newPass) {
		showFeedback("La nuova password non può essere uguale a quella attuale inserita.");
		return;
	}
	if (!passwordRegex.test(newPass)) {
		showFeedback("La password deve avere almeno 8 caratteri, includendo una maiuscola, una minuscola, un numero e un simbolo speciale.");
		return;
	}
	if (newPass !== confPass) {
		showFeedback("La nuova password e la conferma non coincidono.");
		return;
	}
    form.submit();
}

function submitDeletion(button){
	if (!button.classList.contains('confirmButton')){
		button.textContent = "Confermi la cancellazione?";
		button.classList.add('confirmButton');
		confirmTimeout = setTimeout(() => {
			button.textContent = "Cancella l'account";
			button.classList.remove('confirmButton')
		}, 5000);
	} else {
		clearTimeout(confirmTimeout);
		document.getElementById('delete-form').submit();
	}
}