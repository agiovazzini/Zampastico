function submitDeletion(button) {
    if (!button.classList.contains('confirmButton')) {
        button.textContent = "Confermi l'eliminazione?";
        button.classList.add('confirmButton');
        const timeoutId = setTimeout(() => {
            button.textContent = "Elimina Recensione";
            button.classList.remove('confirmButton');
        }, 3000);
    } else {
        clearTimeout(parseInt(button.dataset.timeoutId));
        button.closest('form').submit();
    }
}