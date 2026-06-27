<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Gestione Prodotti - Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin/product.css">
  </head>
  <body>
    <div class="admin-products-container">
      <div class="admin-products-header">
        <h3>Gestione Prodotti</h3>
      </div>
      <div class="tabs-menu">
        <button class="tab-btn active" data-target="tab-add">Aggiungi Nuovo</button>
        <button class="tab-btn" data-target="tab-edit">Modifica</button>
        <button class="tab-btn" data-target="tab-delete">Elimina / Disattiva</button>
      </div>
      <div id="tab-add" class="product-tab-content active">
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
                      <c:forEach begin="1" end="${categoria.livello}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach>↳&nbsp;
                    </c:if>
                    <c:if test="${categoria.livello == 0}">■&nbsp;</c:if>
                    ${categoria.nome}
                  </option>
                </c:forEach>
                <option value="new" class="new-cat-option">+ Aggiungi Nuova Categoria...</option>
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
      <div id="tab-edit" class="product-tab-content" style="display:none;">
        <div class="cascading-selects">
          <select id="edit-cat-select" class="filter-input">
            <option value="" selected disabled>Scegli Categoria</option>
            <c:forEach var="categoria" items="${categorie}">
              <option value="${categoria.idCategoria}" class="cat-level-${categoria.livello}">
                <c:if test="${categoria.livello > 0}">
                  <c:forEach begin="1" end="${categoria.livello}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach>↳&nbsp;
                </c:if>
                <c:if test="${categoria.livello == 0}">■&nbsp;</c:if>
                ${categoria.nome}
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
                        <c:forEach begin="1" end="${categoria.livello}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach>↳&nbsp;
                      </c:if>
                      <c:if test="${categoria.livello == 0}">■&nbsp;</c:if>
                      ${categoria.nome}
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
      <div id="tab-delete" class="product-tab-content" style="display:none;">
        <div class="cascading-selects">
          <select id="del-cat-select" class="filter-input">
            <option value="" selected disabled>Scegli Categoria</option>
            <c:forEach var="categoria" items="${categorie}">
              <option value="${categoria.idCategoria}" class="cat-level-${categoria.livello}">
                <c:if test="${categoria.livello > 0}">
                  <c:forEach begin="1" end="${categoria.livello}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach>↳&nbsp;
                </c:if>
                <c:if test="${categoria.livello == 0}">■&nbsp;</c:if>
                ${categoria.nome}
              </option>
            </c:forEach>
          </select>
          <select id="del-prod-select" class="filter-input" disabled>
            <option value="">Scegli Prodotto</option>
          </select>
          <select id="del-var-select" class="filter-input" disabled>
            <option value="">Scegli Variante</option>
          </select>
        </div>
        <div id="delete-workspace" class="workspace-area" style="display:none;">
          <div class="action-card">
            <h4 id="target-name">Nome Elemento</h4>
            <p id="target-desc">Descrizione elemento selezionato</p>
            <div id="action-buttons-container" class="action-buttons">
              <button id="btn-toggle-status" class="warningButton">Cambia Stato (Attivo/Inattivo)</button>
              <button type="button" id="btn-hard-delete" class="deleteButton">Elimina Definitivamente</button>
            </div>
            <div id="confirm-delete-container" style="display:none;">
              <p>Sei sicuro di voler eliminare definitivamente questo elemento?<br>Questa azione è irreversibile.</p>
              <div>
                <button type="button" id="btn-confirm-yes" class="deleteButton">Sì, Conferma Eliminazione</button>
                <button type="button" id="btn-confirm-no" class="warningButton">Annulla</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script type="module" src="${pageContext.request.contextPath}/scripts/admin/main-product-admin.js"></script>
  </body>
</html>