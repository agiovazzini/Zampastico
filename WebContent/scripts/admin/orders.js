import { showFeedback, hideFeedback } from './ui.js';
import { getOraEsatta} from './coupons.js';


document.addEventListener('DOMContentLoaded', function() {
	const container = document.querySelector('.admin-orders-container');
	if (!container) return;
	const contextPath = container.getAttribute('data-context-path');
	const dateFilterSelect = document.getElementById('dateFilter');
    const customDateRange = document.getElementById('custom-date-range');
    const dateFromInput = document.getElementById('dateFrom');
    const dateToInput = document.getElementById('dateTo');
    if (dateFilterSelect && customDateRange) {
        dateFilterSelect.addEventListener('change', function() {
            customDateRange.style.display = (this.value === 'custom') ? 'grid' : 'none';
            if (this.value !== 'custom') {
                dateFromInput.value = '';
                dateToInput.value = '';
            }
        });
    }

    if (dateFromInput && dateToInput) {
        const dataIntera = getOraEsatta();
		const dataSoloGiorno = dataIntera.split(' ')[0];
		const parti = dataSoloGiorno.split('/');
		const dataDiOggi = `${parti[2]}-${parti[1]}-${parti[0]}`;
        dateFromInput.max = dataDiOggi;
        dateToInput.max = dataDiOggi;
        dateFromInput.addEventListener('change', function() {
            dateToInput.min = this.value || '';
        });
        dateToInput.addEventListener('change', function() {
            dateFromInput.max = this.value || dataDiOggi;
        });
    }
    const selects = document.querySelectorAll('.async-status-select');
    selects.forEach(select => {
        select.dataset.previousValue = select.value;
        select.addEventListener('change', function() {
            const selectElement = this;
            const orderId = selectElement.getAttribute('data-order-id');
            const newState = selectElement.value;
            const previousValue = selectElement.dataset.previousValue;
            selectElement.disabled = true;
            const xhr = new XMLHttpRequest();
            xhr.open('POST', contextPath + '/admin/updateStatus', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    selectElement.disabled = false;
                    if (xhr.status >= 200 && xhr.status < 300) {
                        try {
                            const data = JSON.parse(xhr.responseText);
                            if (data.success) {
                                selectElement.classList.remove('status-' + previousValue);
                                selectElement.classList.add('status-' + newState);
                                selectElement.dataset.previousValue = newState;
                                gestisciMessaggio('Stato dell\'ordine aggiornato con successo.');
                            } else {
                                revertSelect();
                                gestisciMessaggio('Operazione negata o fallita.');
                            }
                        } catch (e) {
                            revertSelect();
                            gestisciMessaggio('Errore di comunicazione col server.');
                        }
                    } else {
                        revertSelect();
                        gestisciMessaggio('Errore di connessione (' + xhr.status + ').');
                    }
                }
            };
            xhr.onerror = function() {
                selectElement.disabled = false;
                revertSelect();
                gestisciMessaggio('Errore di rete.');
            };
            
            function revertSelect() {
                selectElement.value = previousValue;
            }
			
			function gestisciMessaggio(testo) {
				showFeedback(testo);
				setTimeout(hideFeedback, 3500);
			}
            xhr.send('idOrdine=' + orderId + '&stato=' + encodeURIComponent(newState));
        });
    });
});
