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

