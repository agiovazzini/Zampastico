<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head><link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile/temp-orders.css"></head>
<body>
    <div class="orders-wrapper">
        <h3>Il tuo storico ordini</h3>
        <c:if test="${not empty orders}">
            <div class="orders-list">
                <c:forEach var="order" items="${orders}">
                    <div class="order-card">
                        <div class="order-header">
                            <div class="order-info">
                                <span class="order-id">Ordine #ZMP-${order.idOrdine}</span>
                                <span class="order-date">Data: ${order.dataOrdine}</span> 
                            </div>
                            <div class="order-status">
                                <span>Stato: ${order.stato}</span>
                            </div>
                        </div>
                        <div class="order-body">
                            <div class="order-summary">
                                <span class="order-total">Totale: € ${order.totale}</span>
                                <button type="button" class="editButton">Dettagli Ordine</button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${empty orders}">
            <div class="no-orders">
                <p>Non hai ancora effettuato alcun ordine su Zampastico.</p>
            </div>
        </c:if>
    </div>
</body>
</html>