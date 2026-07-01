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
      
		<%@ include file="_product-add.jsp" %>
		<%@ include file="_product-edit.jsp" %>
		<%@ include file="_product-delete.jsp" %>
    </div>
    
    <script type="module" src="${pageContext.request.contextPath}/scripts/admin/main-product-admin.js"></script>
  </body>
</html>