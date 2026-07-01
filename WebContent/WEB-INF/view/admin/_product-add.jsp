<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <div id="tab-add" class="product-tab-content active">

    <form action="${pageContext.request.contextPath}/admin/manageProducts" method="POST" id="form-add-product" enctype="multipart/form-data">
        <input type="hidden" name="action" value="create">
        <div class="form-section-title">Dati Generali Prodotto</div>
        <div class="form-grid">
            <div class="filter-group">
                <label for="idCategoria">Categoria *</label>
                <select id="idCategoria" name="idCategoria" required class="filter-input">
                    <option value="" disabled selected>Seleziona una categoria</option>
                    <c:forEach var="categoria" items="${categorie}">
                        <option value="${categoria.idCategoria}" class="cat-level-${categoria.livello}">
                            <c:if test="${categoria.livello > 0}">
                                <c:forEach begin="1" end="${categoria.livello}">&nbsp;&nbsp;&nbsp;</c:forEach>
                            </c:if>
                            <c:if test="${categoria.livello == 0}">&nbsp;</c:if> ${categoria.nome}
                        </option>
                    </c:forEach>
                    <option value="new" class="new-cat-option">Aggiungi una nuova categoria</option>
                </select>
            </div>
            <div id="new-cat-container" class="full-width hidden" style="display:none;">
                <div class="filter-group">
                    <label for="nuovaCategoria">Nome Nuova Categoria *</label>
                    <input type="text" id="nuovaCategoria" name="nuovaCategoria" class="filter-input" placeholder="Es: Pulcini / Alimentazione">
                </div>
                <div class="filter-group">
                    <label for="nuovaSupercategoria">Sotto a... (Opzionale)</label>
                    <input type="text" id="nuovaSupercategoria" name="nuovaSupercategoria" class="filter-input" placeholder="Es: Uccelli / Cani">
                </div>
            </div>
            <div class="filter-group">
                <label for="nome">Nome Prodotto *</label>
                <input type="text" id="nome" name="nome" required placeholder="Es: Collare in pelle" class="filter-input">
            </div>
            <div class="filter-group">
                <label for="marca">Brand *</label>
                <input type="text" id="marca" name="marca" required placeholder="Es: Zampastico" class="filter-input">
            </div>
            <div class="filter-group checkbox-group">
                <label>Visibilità Prodotto:</label>
                <label class="switch">
                    <input type="checkbox" name="prodottoAttivo" checked>
                    <span class="slider round"></span>
                </label>
                <span class="toggle-label">Attivo nel catalogo</span>
            </div>
        </div>
        <div class="filter-group full-width">
            <label for="descrizione">Descrizione *</label>
            <textarea id="descrizione" name="descrizione" required class="filter-input textarea-input"></textarea>
        </div>
        <div class="filter-group full-width image-upload-container">
            <label for="immagineProdotto">Immagine Principale Prodotto (Opzionale)</label>
            <input type="file" id="immagineProdotto" name="immagineProdotto" accept="image/*" class="filter-input">
            <div id="wrapper-preview-prod" class="preview-box">
                <span class="debug-info">Anteprima:</span>
                <img id="debug-preview-prod" src="" class="image-preview preview-new" alt="Anteprima Prodotto" />
            </div>
        </div>
        <div class="form-section-title variant-title">Prima Variante Obbligatoria</div>
        <div class="form-grid variant-box">
            <div class="filter-group">
                <label for="formato">Formato / Taglia *</label>
                <input type="text" id="formato" name="formato" required placeholder="Es: Taglia M / 500g" class="filter-input">
            </div>
            <div class="filter-group">
                <label for="prezzo">Prezzo di Listino (&euro;) *</label>
                <input type="number" id="prezzo" name="prezzo" step="0.01" min="0.01" required placeholder="0.00" class="filter-input">
            </div>
            <div class="filter-group checkbox-group">
                <label>Stato Variante:</label>
                <label class="switch">
                    <input type="checkbox" name="varianteDisponibile" checked>
                    <span class="slider round"></span>
                </label>
                <span class="toggle-label">Disponibile per l'acquisto</span>
            </div>
            <div class="filter-group full-width image-upload-container">
                <label for="immagineVariante">Immagine Variante (Opzionale)</label>
                <input type="file" id="immagineVariante" name="immagineVariante" accept="image/*" class="filter-input">
                <div id="wrapper-preview-var" class="preview-box">
                    <span class="debug-info">Anteprima:</span>
                    <img id="debug-preview-var" src="" class="image-preview preview-new" alt="Anteprima Variante" />
                </div>
            </div>
        </div>
        <div class="form-actions">
            <button type="submit" class="saveButton">Crea Prodotto e Variante</button>
        </div>
    </form>
</div>