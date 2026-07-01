<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin/orders.css">                      
    </head>
    <body>
        <div class="admin-orders-container" data-context-path="${pageContext.request.contextPath}">
            <div class="admin-orders-header">
                <h3>Pannello Controllo Ordini</h3>
            </div>
            <div class="filter-section">
                <form action="${pageContext.request.contextPath}/admin/orders" method="GET" class="admin-filter-bar">
                    <div class="filter-group">
                        <label for="emailFilter">Cerca per Email</label>
                        <input type="text" id="emailFilter" name="emailFilter" value="${emailFilter}" placeholder="Es: utente@email.com" class="filter-input">
                    </div>
                    <div class="filter-group">
                        <label for="dateFilter">Filtro Data</label>
                        <select name="dateFilter" id="dateFilter" class="filter-select">
                            <option value="all" ${dateFilter=='all' ? 'selected' : '' }>Tutti i tempi</option>
                            <option value="oggi" ${dateFilter=='oggi' ? 'selected' : '' }>Oggi</option>
                            <option value="ieri" ${dateFilter=='ieri' ? 'selected' : '' }>Ieri</option>
                            <option value="ultimi_7" ${dateFilter=='ultimi_7' ? 'selected' : '' }>Ultimi 7 giorni</option>
                            <option value="ultimo_mese" ${dateFilter=='ultimo_mese' ? 'selected' : '' }>Ultimo mese</option>
                            <option value="ultimo_anno" ${dateFilter=='ultimo_anno' ? 'selected' : '' }>Ultimo anno</option>
                            <option value="custom" ${dateFilter=='custom' ? 'selected' : '' }>Intervallo personalizzato (Da - A)</option>
                        </select>
                    </div>
                    <div id="custom-date-range" class="filter-group date-range-group">
                        <div class="date-input-wrapper">
                            <label for="dateFrom">Da</label>
                            <input type="date" id="dateFrom" name="dateFrom" value="${dateFrom}" class="filter-input">
                        </div>
                        <div class="date-input-wrapper">
                            <label for="dateTo">A</label>
                            <input type="date" id="dateTo" name="dateTo" value="${dateTo}" class="filter-input">
                        </div>
                    </div>
                    <div class="filter-actions">
                        <button type="submit" class="saveButton filter-btn">Applica Filtri</button>
                        <a href="${pageContext.request.contextPath}/admin/orders" class="defaultButton reset-btn">Reset</a>
                    </div>
                </form>
            </div>
            <c:if test="${not empty adminOrders}">
                <div class="table-responsive">
                    <table class="admin-orders-table">
                        <thead>
                            <tr>
                                <th>ID Ordine</th>
                                <th>Email Utente</th>
                                <th>Data Ordine</th>
                                <th>Coupon</th>
                                <th>Totale</th>
                                <th>Stato Spedizione</th>
                                <th>Indirizzo Consegna</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${adminOrders}">
                                <tr>
                                    <td class="order-id-cell">${order.idOrdine}</td>
                                    <td class="order-email-cell">${order.emailUtente}</td>
                                    <td class="order-date-cell">${order.dataOrdine}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty order.idCoupon}">
                                                <span class="coupon-ticket">
                                                    <span class="coupon-icon">🎟️</span> ID: ${order.idCoupon} </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="no-coupon">&mdash;</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="order-price-cell">€ ${order.totale}</td>
                                    <td>
                                        <select data-order-id="${order.idOrdine}" class="status-select status-${order.stato} async-status-select">
                                            <option value="in_attesa" ${order.stato=='in_attesa' ? 'selected' : '' }>In Attesa</option>
                                            <option value="pagato" ${order.stato=='pagato' ? 'selected' : '' }>Pagato</option>
                                            <option value="in_lavorazione" ${order.stato=='in_lavorazione' ? 'selected' : '' }>In Lavorazione</option>
                                            <option value="spedito" ${order.stato=='spedito' ? 'selected' : '' }>Spedito</option>
                                            <option value="consegnato" ${order.stato=='consegnato' ? 'selected' : '' }>Consegnato</option>
                                            <option value="annullato" ${order.stato=='annullato' ? 'selected' : '' }>Annullato</option>
                                        </select>
                                    </td>
                                    <td class="order-address-cell" title="${order.spedizioneVia}, ${order.spedizioneCitta}">${order.spedizioneVia}, ${order.spedizioneCitta}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <c:if test="${noOfPages > 1}">
                    <div class="pagination">
                        <c:set var="urlParams" value="&emailFilter=${emailFilter != null ? emailFilter : ''}&dateFilter=${dateFilter}&dateFrom=${dateFrom != null ? dateFrom : ''}&dateTo=${dateTo != null ? dateTo : ''}" />
                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/admin/orders?page=${currentPage - 1}${urlParams}" class="page-link">&laquo; Precedente</a>
                        </c:if>
                        <c:forEach begin="1" end="${noOfPages}" var="i">
                            <c:choose>
                                <c:when test="${currentPage eq i}">
                                    <span class="page-link active">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/admin/orders?page=${i}${urlParams}" class="page-link">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage < noOfPages}">
                            <a href="${pageContext.request.contextPath}/admin/orders?page=${currentPage + 1}${urlParams}" class="page-link">Successivo &raquo;</a>
                        </c:if>
                    </div>
                </c:if>
            </c:if>
            <c:if test="${empty adminOrders}">
                <div class="no-orders-alert">
                    <p>Nessun ordine corrisponde ai criteri di ricerca.</p>
                </div>
            </c:if>
        </div>
        <script type="module" src="${pageContext.request.contextPath}/scripts/admin/orders.js"></script>
    </body>
</html>