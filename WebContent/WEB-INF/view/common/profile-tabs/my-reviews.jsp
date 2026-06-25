<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
    <div class="reviews-container">
        <h3>Le tue recensioni</h3>
        <c:if test="${not empty userReviews}">
            <div class="reviews-list">
                <c:forEach var="recensione" items="${userReviews}">
                    <div class="review-card">
                        <div class="review-header">
                            <div class="review-product-info">
                                <span class="product-title">
                                    <span class="product-brand">${recensione.brandProdotto}</span> - ${recensione.nomeProdotto}
                                    <span class="product-format">(${recensione.formatoProdotto})</span>
                                </span>
                            </div>
                            <div class="review-stars">
                                <c:forEach begin="1" end="5" var="i">
                                    <span class="star ${i <= recensione.votazione ? 'filled' : ''}">★</span>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="review-body">
                            <p class="review-text">${recensione.commento}</p>
                        </div>
                        <div class="review-footer">
                            <span class="review-date">Recensito il: ${recensione.dataCreazione}</span>
                            <form action="${pageContext.request.contextPath}/DeleteReview" method="POST">
                                <input type="hidden" name="idVarianteProdotto" value="${recensione.idVarianteProdotto}">
                                <button type="button" class="deleteButton" onclick="submitDeletion(this)">Elimina Recensione</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${empty userReviews}">
            <div class="no-reviews">
                <p>Non hai ancora scritto nessuna recensione su Zampastico.</p>
            </div>
        </c:if>
    </div>
    <script src="${pageContext.request.contextPath}/scripts/profile/my-reviews.js"></script>
</body>
</html>