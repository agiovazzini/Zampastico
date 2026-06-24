document.addEventListener("DOMContentLoaded", function() {
    const detailsButtons = document.querySelectorAll('.detailsButton');
    
    detailsButtons.forEach(button => {
        button.addEventListener('click', function() {
            const orderBody = button.closest('.order-body');
            const detailsPanel = orderBody.querySelector('.order-details');
            if (detailsPanel) {
                button.classList.toggle('active');
                detailsPanel.classList.toggle('open');
                
                if (detailsPanel.classList.contains('open')) {
                    button.textContent = "Nascondi dettagli prodotti";
                } else {
                    button.textContent = "Visualizza dettagli prodotti";
                }
            }
        });
    });
});