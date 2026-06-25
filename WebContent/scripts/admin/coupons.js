document.addEventListener('DOMContentLoaded', function() {
    const dataScadenzaInput = document.getElementById('dataScadenza');
    
    if (dataScadenzaInput) {
        const now = new Date();
        const tzOffset = now.getTimezoneOffset() * 60000; 
        const localISOTime = new Date(now - tzOffset).toISOString().slice(0, 16);
        dataScadenzaInput.min = localISOTime;
        dataScadenzaInput.addEventListener('change', function() {
            if (this.value && this.value < localISOTime) {
                this.value = localISOTime;
            }
        });
    }
});