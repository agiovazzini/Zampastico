<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="jakarta.tags.core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin/_product-edit.css">
    </head>
    <body>
<div id="tab-edit" class="product-tab-content" style="display:none;">
    <div class="cascading-selects">
        <select id="edit-cat-select" class="filter-input">
            <option value="" selected disabled>Scegli Categoria</option>
            <c:forEach var="categoria" items="${categorie}">
                <option value="${categoria.idCategoria}" class="cat-level-${categoria.livello}">
                    <c:if test="${categoria.livello > 0}">
                        <c:forEach begin="1" end="${categoria.livello}">&nbsp;&nbsp;&nbsp;</c:forEach>
                    </c:if>
                    <c:if test="${categoria.livello == 0}">&nbsp;</c:if> ${categoria.nome}
                </option>
            </c:forEach>
        </select>
        <select id="edit-prod-select" class="filter-input" disabled>
            <option value="">Scegli Prodotto</option>
        </select>
        <select id="edit-var-select" class="filter-input" disabled>
            <option value="">Scegli Variante</option>
        </select>
    </div>
    <div id="edit-workspace" class="workspace-area">
        <p id="edit-placeholder" class="placeholder-text">Seleziona un prodotto per modificarne i dettagli.</p>
        <form id="form-update-product" class="admin-product-form" style="display:none;">
            <input type="hidden" id="edit-hidden-idProd">
            <div class="form-section-title">Modifica Prodotto</div>
            <div class="form-grid">
                <div class="filter-group">
                    <label for="edit-idCategoria">Categoria *</label>
                    <select id="edit-idCategoria" name="idCategoria" required class="filter-input">
                        <option value="" disabled>Scegli Nuova Categoria</option>
                        <c:forEach var="categoria" items="${categorie}">
                            <option value="${categoria.idCategoria}" class="cat-level-${categoria.livello}">
                                <c:if test="${categoria.livello > 0}">
                                    <c:forEach begin="1" end="${categoria.livello}">&nbsp;&nbsp;&nbsp;</c:forEach>
                                </c:if>
                                <c:if test="${categoria.livello == 0}">&nbsp;</c:if> ${categoria.nome}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="filter-group">
                    <label for="edit-nome">Nome Prodotto *</label>
                    <input type="text" id="edit-nome" required class="filter-input">
                </div>
                <div class="filter-group">
                    <label for="edit-marca">Brand *</label>
                    <input type="text" id="edit-marca" required class="filter-input">
                </div>
                <div class="filter-group checkbox-group">
                    <label>Visibilità Prodotto:</label>
                    <label class="switch">
                        <input type="checkbox" id="edit-prodottoAttivo">
                        <span class="slider round"></span>
                    </label>
                    <span class="toggle-label">Attivo nel catalogo</span>
                </div>
            </div>
            <div class="filter-group full-width">
                <label for="edit-descrizione">Descrizione *</label>
                <textarea id="edit-descrizione" required class="filter-input textarea-input"></textarea>
            </div>
            <div class="filter-group full-width image-upload-container">
                <label for="edit-immagineProdotto">Modifica Immagine Prodotto</label>
                <input type="hidden" id="remove-img-prod-flag" value="false">
                <input type="file" id="edit-immagineProdotto" name="immagineProdotto" accept="image/*" class="filter-input">
                <div class="previews-container">
                    <div id="wrapper-edit-img-current" class="preview-box">
                        <span class="debug-info">Immagine Attuale:</span>
                        <img id="edit-img-current" src="" alt="Attuale" class="image-preview preview-edit-current" />
                        <br>
                        <button type="button" id="btn-remove-edit-img-prod" class="warningButton btn-remove-img">Rimuovi Attuale</button>
                    </div>
                    <div id="wrapper-edit-img-new" class="preview-box">
                        <span class="debug-info">Nuova Immagine:</span>
                        <img id="edit-img-new" src="" alt="Nuova" class="image-preview preview-edit-new" />
                    </div>
                </div>
            </div>
            <div class="form-actions">
                <button type="submit" class="saveButton">Salva Modifiche Prodotto</button>
                <button type="button" id="btn-show-add-variant" class="saveButton">+ Aggiungi Nuova Variante</button>
            </div>
        </form>
        <form id="form-update-variant" class="admin-product-form" style="display:none;">
            <input type="hidden" id="edit-hidden-idVar">
            <div class="form-section-title variant-title">Modifica Variante</div>
            <div class="form-grid variant-box">
                <div class="filter-group">
                    <label for="edit-formato">Formato / Taglia *</label>
                    <input type="text" id="edit-formato" required class="filter-input">
                </div>
                <div class="filter-group">
                    <label for="edit-prezzo">Prezzo di Listino (&euro;) *</label>
                    <input type="number" step="0.01" min="0.01" id="edit-prezzo" required class="filter-input">
                </div>
                <div class="filter-group checkbox-group">
                    <label>Stato Variante:</label>
                    <label class="switch">
                        <input type="checkbox" id="edit-varianteDisponibile">
                        <span class="slider round"></span>
                    </label>
                    <span class="toggle-label">Disponibile per l'acquisto</span>
                </div>
            </div>
            <div class="filter-group full-width image-upload-container">
                <label for="edit-immagineVariante">Modifica Immagine Variante</label>
                <input type="hidden" id="remove-img-var-flag" value="false">
                <input type="file" id="edit-immagineVariante" name="immagineVariante" accept="image/*" class="filter-input">
                <div class="previews-container">
                    <div id="wrapper-edit-var-img-current" class="preview-box">
                        <span class="debug-info">Immagine Attuale:</span>
                        <img id="edit-var-img-current" src="" alt="Attuale" class="image-preview preview-edit-current" />
                        <br>
                        <button type="button" id="btn-remove-edit-img-var" class="warningButton btn-remove-img">Rimuovi Attuale</button>
                    </div>
                    <div id="wrapper-edit-var-img-new" class="preview-box">
                        <span class="debug-info">Nuova Immagine:</span>
                        <img id="edit-var-img-new" src="" alt="Nuova" class="image-preview preview-edit-new" />
                    </div>
                </div>
            </div>
            <div class="form-actions">
                <button type="submit" class="saveButton">Salva Variante</button>
                <button type="button" id="btn-back-from-edit-var" class="warningButton">Torna al Prodotto</button>
            </div>
        </form>
        <form id="form-create-variant" class="admin-product-form" style="display:none;">
            <div class="form-section-title variant-title">Aggiungi Nuova Variante</div>
            <div class="form-grid variant-box">
                <div class="filter-group">
                    <label for="new-formato">Formato / Taglia *</label>
                    <input type="text" id="new-formato" required class="filter-input" placeholder="Es: Taglia L / 1kg">
                </div>
                <div class="filter-group">
                    <label for="new-prezzo">Prezzo di Listino (&euro;) *</label>
                    <input type="number" step="0.01" min="0.01" id="new-prezzo" required class="filter-input" placeholder="0.00">
                </div>
                <div class="filter-group checkbox-group">
                    <label>Stato Variante:</label>
                    <label class="switch">
                        <input type="checkbox" id="new-varianteDisponibile" checked>
                        <span class="slider round"></span>
                    </label>
                    <span class="toggle-label">Disponibile per l'acquisto</span>
                </div>
            </div>
            <div class="filter-group full-width image-upload-container">
                <label for="new-immagineVariante">Immagine Variante (Opzionale)</label>
                <input type="file" id="new-immagineVariante" name="immagineVariante" accept="image/*" class="filter-input">
                <div id="wrapper-new-var-img-preview" class="preview-box">
                    <span class="debug-info">Anteprima:</span>
                    <img id="new-var-img-preview" src="" class="image-preview preview-new" alt="Anteprima Nuova Variante" />
                </div>
            </div>
            <div class="form-actions">
                <button type="submit" class="saveButton">Crea Variante</button>
                <button type="button" id="btn-cancel-create-var" class="warningButton">Annulla</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>