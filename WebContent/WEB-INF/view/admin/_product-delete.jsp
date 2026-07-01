<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin/_product-delete.css">
    </head>
    <body>
<div id="tab-delete" class="product-tab-content" style="display:none;">
        <div class="cascading-selects">
          <select id="del-cat-select" class="filter-input">
            <option value="" selected disabled>Scegli Categoria</option>
            <c:forEach var="categoria" items="${categorie}">
              <option value="${categoria.idCategoria}" class="cat-level-${categoria.livello}">
                <c:if test="${categoria.livello > 0}">
                  <c:forEach begin="1" end="${categoria.livello}">&nbsp;&nbsp;&nbsp;</c:forEach>
                </c:if>
                <c:if test="${categoria.livello == 0}">&nbsp;</c:if>
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
    </body>
    </html>