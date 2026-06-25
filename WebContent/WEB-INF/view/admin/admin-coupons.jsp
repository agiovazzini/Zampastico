<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin/coupons.css">
    </head>
    <body>
        <div class="admin-orders-container">
            <div class="admin-orders-header">
                <h3>Gestione Coupon</h3>
            </div>
            <div class="coupons-layout">
                <div class="filter-section">
                    <h4>Crea Nuovo Coupon</h4>
                    <form action="${pageContext.request.contextPath}/admin/manageCoupons" method="POST" class="admin-filter-bar">
                        <input type="hidden" name="action" value="create">
                        <div class="vertical-form-group">
                            <div class="filter-group">
                                <label for="codice">Codice Promozionale</label>
                                <input type="text" id="codice" name="codice" required placeholder="Es: SCONTO20" class="filter-input">
                            </div>
                            <div class="filter-group">
                                <label for="sconto">Sconto (es: 0.20 per 20%)</label>
                                <input type="number" id="sconto" name="sconto" min="0.01" max="1.00" step="0.01" required placeholder="0.20" class="filter-input">
                            </div>
                            <div class="filter-group">
                                <label for="dataScadenza">Data Scadenza (Opzionale)</label>
                                <input type="datetime-local" id="dataScadenza" name="dataScadenza" class="filter-input">
                            </div>
                            <div class="filter-actions">
                                <button type="submit" class="saveButton filter-btn">Genera Coupon</button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="coupons-list-section">
                    <c:if test="${not empty coupons}">
                        <div class="table-responsive">
                            <table class="admin-orders-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Codice</th>
                                        <th>Percentuale Sconto</th>
                                        <th>Scadenza</th>
                                        <th>Azioni</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="coupon" items="${coupons}">
                                        <tr class="${coupon.scaduto ? 'expired-row' : ''}">
                                            <td class="order-id-cell">${coupon.idCoupon}</td>
                                            <td>
                                                <div>
                                                    <span class="coupon-ticket ${coupon.scaduto ? 'expired-ticket' : ''}">
                                                        <span class="coupon-icon">🎟️</span>
                                                        <span class="coupon-text">${coupon.codice}</span>
                                                    </span>
                                                    <c:if test="${coupon.scaduto}">
                                                        <span class="expired-badge">Scaduto</span>
                                                    </c:if>
                                                </div>
                                            </td>
                                            <td class="order-price-cell">${String.format("%.0f", coupon.percentualeSconto * 100)}%</td>
                                            <td class="order-date-cell">${coupon.dataScadenza}</td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/admin/manageCoupons" method="POST">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="idCoupon" value="${coupon.idCoupon}">
                                                    <button type="submit" class="deleteButton">Elimina</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <c:if test="${noOfPages > 1}">
                            <div class="pagination-container">
                                <c:if test="${currentPage > 1}">
                                    <a href="${pageContext.request.contextPath}/admin/coupons?page=${currentPage - 1}" class="page-link">&laquo; Precedente</a>
                                </c:if>
                                <c:forEach begin="1" end="${noOfPages}" var="i">
                                    <c:choose>
                                        <c:when test="${currentPage eq i}">
                                            <span class="page-link active">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/admin/coupons?page=${i}" class="page-link">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <c:if test="${currentPage < noOfPages}">
                                    <a href="${pageContext.request.contextPath}/admin/coupons?page=${currentPage + 1}" class="page-link">Successivo &raquo;</a>
                                </c:if>
                            </div>
                        </c:if>
                    </c:if>
                    <c:if test="${empty coupons}">
                        <div class="no-orders-alert">
                            <p>Non ci sono coupon attivi o in archivio.</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/scripts/admin/coupons.js"></script>
    </body>
</html>