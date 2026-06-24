<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<body>
	<div class="orders-container">
	    <h3>Il tuo storico ordini</h3>
	    <c:if test="${not empty orders}">
	        <div class="orders-list">
	            <c:forEach var="order" items="${orders}">
	                <div class="order-card">
	                    <div class="order-main">
	                        <div class="order-info">
	                            <span class="order-id">Ordine #ZMP-${order.idOrdine}</span>
	                            <span class="order-date">Effettuato il: ${order.dataOrdine}</span> 
	                        </div>
	                        <div class="order-status status-${order.stato}">
	                            ${order.statoVisualizzabile}
	                        </div>
	                    </div>
	                    <div class="order-body">
	                        <div class="order-summary">
	                            <div class="order-payment-details">
	                                <span class="order-total">Totale: <span class="price-amount">€ ${order.totale}</span></span>
	                                <c:if test="${not empty order.metodoPagamento}">
	                                    <span class="order-payment">
	                                        ${order.metodoPagamentoVisualizzabile}
	                                    </span>
	                                </c:if>
	                            </div>
	                            <button class="detailsButton">Visualizza dettagli prodotti</button>
	                        </div>
	                        
	                        <div class="order-details">
	                            <div class="order-products">
	                                <h4>Prodotti Acquistati</h4>
	                                <div class="itemslist">
	                                    <c:forEach var="voce" items="${order.vociOrdine}">
	                                        <div class="item-container">
	                                            <div class="item-info">
	                                                <span class="list-item-name">
	                                                    <span class="product-brand">${voce.brand}</span>- ${voce.nomeProdotto} 
	                                                    <span class="product-format">(${voce.formato})</span>
	                                                </span>
	                                                <span class="item-amount">${voce.quantita}</span>
	                                            </div>
	                                            <span class="list-item-price">€ ${voce.prezzoAcquisto}</span>
	                                        </div>
	                                    </c:forEach>
	                                </div>
	                            </div>
	                        </div> </div> </div> </c:forEach>
	        </div>
	    </c:if>
	    
	    <c:if test="${empty orders}">
	        <div class="no-orders">
	            <p>Non hai ancora effettuato alcun ordine su Zampastico.</p>
	        </div>
	    </c:if>
	</div>
	
	<script src="${pageContext.request.contextPath}/scripts/profile/my-orders.js"></script>
</body>
</html>