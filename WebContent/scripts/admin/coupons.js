document.addEventListener('DOMContentLoaded', function() {
    const dataScadenzaInput = document.getElementById('dataScadenza');
    if (dataScadenzaInput) {
        dataScadenzaInput.min = getOraEsatta();
        dataScadenzaInput.addEventListener('change', function() {
			let oraAttuale = getOraEsatta();
            if (this.value && this.value < oraAttuale) {
                this.value = oraAttuale;
            }
        });
    }
});

export function getOraEsatta() {
	var today = new Date();
	var yyyy = today.getFullYear();
	var mm = String(today.getMonth() + 1).padStart(2, '0');
	var dd = String(today.getDate()).padStart(2, '0');
	var hh = String(today.getHours()).padStart(2, '0');
	var min = String(today.getMinutes()).padStart(2, '0');
	return dd + '/' + mm + '/' + yyyy + ' ' + hh + ':' + min;
}