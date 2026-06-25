<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div class="admin-orders-container">
    <div class="admin-orders-header">
        <h3>Gestione Coupon</h3>
    </div>
    <div class="filter-section" style="margin-bottom: 30px;">
        <h4 style="margin-top:0; color: var(--color-primary); font-size: 1.1rem;">Crea Nuovo Coupon</h4>
        <form action="${pageContext.request.contextPath}/admin/manageCoupons" method="POST" class="admin-filter-bar" style="align-items: end;">
            <input type="hidden" name="action" value="create">
            
            <div class="filter-group">
                <label for="codice">Codice Promozionale</label>
                <input type="text" id="codice" name="codice" required placeholder="Es: SCONTO20" class="filter-input" style="text-transform: uppercase;">
            </div>

            <div class="filter-group">
                <label for="sconto">Sconto (es: 0.20 per 20%)</label>
                <input type="number" id="sconto" name="sconto" min="0.01" max="1.00" step="0.01" required placeholder="0.20" class="filter-input">
            </div>

            <div class="filter-group">
                <label for="dataScadenza">Data Scadenza (Opzionale)</label>
                <input type="datetime-local" id="dataScadenza" name="dataScadenza" class="filter-input">
            </div>

            <div class="filter-actions" style="grid-template-columns: 1fr;">
                <button type="submit" class="saveButton filter-btn">Genera Coupon</button>
            </div>
        </form>
    </div>

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
                        <tr>
                            <td class="order-id-cell">${coupon.idCoupon}</td>
                            <td>
                                <span class="coupon-ticket">
                                    <span class="coupon-icon">🎟️</span> ${coupon.codice}
                                </span>
                            </td>
                            <td class="order-price-cell">${coupon.percentualeSconto * 100}%</td>
                            <td class="order-date-cell">${coupon.dataScadenza}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/manageCoupons" method="POST" style="margin:0;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="idCoupon" value="${coupon.idCoupon}">
                                    <button type="submit" class="deleteButton" onclick="return confirm('Sei sicuro di voler eliminare questo coupon?');">Elimina</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
    
    <c:if test="${empty coupons}">
        <div class="no-orders-alert">
            <p>Non ci sono coupon attivi o in archivio.</p>
        </div>
    </c:if>
</div>