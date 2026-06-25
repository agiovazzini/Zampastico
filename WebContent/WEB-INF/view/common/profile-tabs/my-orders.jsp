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
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </div>
        <c:if test="${noOfPagesOrders > 1}">
            <div class="pagination-container">
                <c:if test="${currentPageOrders > 1}">
                    <a href="${pageContext.request.contextPath}/profile/orders?page=${currentPageOrders - 1}" class="page-link">&laquo; Precedente</a>
                </c:if>
                <c:forEach begin="1" end="${noOfPagesOrders}" var="i">
                    <c:choose>
                        <c:when test="${i == 1 || i == noOfPagesOrders || (i >= currentPageOrders - 1 && i <= currentPageOrders + 1)}">
                            <c:choose>
                                <c:when test="${currentPageOrders eq i}">
                                    <span class="page-link active">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/profile/orders?page=${i}" class="page-link">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:when test="${(i == 2 && currentPageOrders > 3) || (i == noOfPagesOrders - 1 && currentPageOrders < noOfPagesOrders - 2)}">
                            <span class="page-ellipsis">&hellip;</span>
                        </c:when>
                    </c:choose>
                </c:forEach>
                <c:if test="${currentPageOrders < noOfPagesOrders}">
                    <a href="${pageContext.request.contextPath}/profile/orders?page=${currentPageOrders + 1}" class="page-link">Successivo &raquo;</a>
                </c:if>
            </div>
        </c:if>
        <c:if test="${empty orders}">
            <div class="no-orders">
                <p>Non hai ancora effettuato alcun ordine su Zampastico.</p>
            </div>
        </c:if>
        <script src="${pageContext.request.contextPath}/scripts/profile/my-orders.js"></script>
    </body>
</html>