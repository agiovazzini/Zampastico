import { showFeedback } from './ui.js';

export function fetchAjax(action, params, callback) {
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "manageProducts", true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            try {
                callback(JSON.parse(xhr.responseText));
            } catch (e) {
                showFeedback("Errore funzionalità AJAX.");
            }
        }
    };
    if (params instanceof FormData) {
        params.append('action', action);
        xhr.send(params);
    } else {
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhr.send(`action=${action}&${params}`);
    }
}