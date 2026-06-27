import { initTabs,	initAllImagePreviews } from './ui.js';
import { initCategoriesLogic } from './categories.js';
import { initProductsEditLogic } from './products-edit.js';
import { initProductsDeleteLogic } from './products-delete.js';

document.addEventListener("DOMContentLoaded", function() {
    initTabs();
	initAllImagePreviews();
    initCategoriesLogic();
    initProductsEditLogic();
    initProductsDeleteLogic();
});