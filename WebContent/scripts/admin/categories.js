export function initCategoriesLogic() {
    const idCategoriaSelect = document.getElementById('idCategoria');
    const newCatContainer = document.getElementById('new-cat-container');
    const nuovaCategoriaInput = document.getElementById('nuovaCategoria');
    const nuovaSupercategoriaInput = document.getElementById('nuovaSupercategoria');
    if (idCategoriaSelect && newCatContainer && nuovaCategoriaInput && nuovaSupercategoriaInput) {
        idCategoriaSelect.addEventListener('change', function() {
            if (this.value === 'new') {
                newCatContainer.style.display = 'grid'; 
                nuovaCategoriaInput.required = true;       
                nuovaSupercategoriaInput.required = false; 
                nuovaCategoriaInput.focus();
            } else {
                newCatContainer.style.display = 'none';
                nuovaCategoriaInput.required = false;
                nuovaSupercategoriaInput.required = false;
                nuovaCategoriaInput.value = '';
                nuovaSupercategoriaInput.value = '';
            }
        });
    }
}