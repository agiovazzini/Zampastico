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
        const now = new Date();
        const today = new Date(now - now.getTimezoneOffset() * 60000).toISOString().split('T')[0];
        dateFromInput.max = today;
        dateToInput.max = today;
        dateFromInput.addEventListener('change', function() {
            dateToInput.min = this.value || '';
        });
        dateToInput.addEventListener('change', function() {
            dateFromInput.max = this.value || today;
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
            selectElement.style.opacity = '0.6';
            selectElement.disabled = true;
            const xhr = new XMLHttpRequest();
            xhr.open('POST', contextPath + '/admin/updateStatus', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    selectElement.style.opacity = '1';
                    selectElement.disabled = false;
                    if (xhr.status >= 200 && xhr.status < 300) {
                        try {
                            const data = JSON.parse(xhr.responseText);
                            if (data.success) {
                                selectElement.classList.remove('status-' + previousValue);
                                selectElement.classList.add('status-' + newState);
                                selectElement.dataset.previousValue = newState;
                                showDynamicFeedback('Stato dell\'ordine aggiornato con successo.', 'success');
                            } else {
                                revertSelect();
                                showDynamicFeedback('Operazione negata o fallita.', 'error');
                            }
                        } catch (e) {
                            revertSelect();
                            showDynamicFeedback('Errore di comunicazione col server.', 'error');
                        }
                    } else {
                        revertSelect();
                        showDynamicFeedback('Errore di connessione (' + xhr.status + ').', 'error');
                    }
                }
            };
            xhr.onerror = function() {
                selectElement.style.opacity = '1';
                selectElement.disabled = false;
                revertSelect();
                showDynamicFeedback('Errore di rete.', 'error');
            };
            
            function revertSelect() {
                selectElement.value = previousValue;
            }
            xhr.send('idOrdine=' + orderId + '&stato=' + encodeURIComponent(newState));
        });
    });
    function showDynamicFeedback(message, type) {
        const tabContent = document.querySelector('.tab-content');
        if (!tabContent) return; 
        let existingFeedback = tabContent.querySelector('.feedback-div');
        if (existingFeedback) {
            existingFeedback.remove();
        }
        const feedbackDiv = document.createElement('div');
        feedbackDiv.className = 'feedback-div';
        feedbackDiv.textContent = message;
        if (type === 'error') {
            feedbackDiv.style.borderLeftColor = '#e74c3c';
        } else {
            feedbackDiv.style.borderLeftColor = 'var(--color-primary)';
        }

        tabContent.insertBefore(feedbackDiv, tabContent.firstChild);
    }
});