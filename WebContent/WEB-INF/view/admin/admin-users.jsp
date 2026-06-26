<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin/users.css">
    </head>
    <body>
        <div class="admin-orders-container">
            <div class="admin-orders-header">
                <h3>Gestione Utenti Registrati</h3>
            </div>
            <div class="users-layout">
                <div class="users-column">
                    <div class="column-header admin-header">
                        <h4>Amministratori</h4>
                    </div>
                    <c:if test="${not empty adminUsers}">
                        <div class="table-responsive">
                            <table class="admin-orders-table users-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Utente</th>
                                        <th>Stato</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="admin" items="${adminUsers}">
                                        <tr>
                                            <td class="order-id-cell">#${admin.idUtente}</td>
                                            <td>
                                                <div class="user-info-cell">
                                                    <span class="user-name">${admin.nome} ${admin.cognome}</span>
                                                    <span class="user-email">${admin.email}</span>
                                                </div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${admin.attivo}">
                                                        <span class="status-badge active-user">Attivo</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge inactive-user">Disattivato</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <c:if test="${noOfPagesAdmin > 1}">
                            <div class="pagination-container column-pagination">
                                <c:if test="${currentPageAdmin > 1}">
                                    <a href="${pageContext.request.contextPath}/admin/users?pageAdmin=${currentPageAdmin - 1}&pageClient=${currentPageClient}" class="page-link">&laquo;</a>
                                </c:if>
                                <c:forEach begin="1" end="${noOfPagesAdmin}" var="i">
                                    <c:choose>
                                        <c:when test="${currentPageAdmin eq i}">
                                            <span class="page-link active">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/admin/users?pageAdmin=${i}&pageClient=${currentPageClient}" class="page-link">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <c:if test="${currentPageAdmin < noOfPagesAdmin}">
                                    <a href="${pageContext.request.contextPath}/admin/users?pageAdmin=${currentPageAdmin + 1}&pageClient=${currentPageClient}" class="page-link">&raquo;</a>
                                </c:if>
                            </div>
                        </c:if>
                    </c:if>
                    <c:if test="${empty adminUsers}">
                        <div class="no-data-card">Nessun amministratore in questa pagina.</div>
                    </c:if>
                </div>
                <div class="users-column">
                    <div class="column-header client-header">
                        <h4>Clienti</h4>
                    </div>
                    <c:if test="${not empty clientUsers}">
                        <div class="table-responsive">
                            <table class="admin-orders-table users-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Utente</th>
                                        <th>Stato</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="client" items="${clientUsers}">
                                        <tr>
                                            <td class="order-id-cell">#${client.idUtente}</td>
                                            <td>
                                                <div class="user-info-cell">
                                                    <span class="user-name">${client.nome} ${client.cognome}</span>
                                                    <span class="user-email">${client.email}</span>
                                                </div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${client.attivo}">
                                                        <span class="status-badge active-user">Attivo</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge inactive-user">Disattivato</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <c:if test="${noOfPagesClient > 1}">
                            <div class="pagination-container column-pagination">
                                <c:if test="${currentPageClient > 1}">
                                    <a href="${pageContext.request.contextPath}/admin/users?pageAdmin=${currentPageAdmin}&pageClient=${currentPageClient - 1}" class="page-link">&laquo;</a>
                                </c:if>
                                <c:forEach begin="1" end="${noOfPagesClient}" var="i">
                                    <c:choose>
                                        <c:when test="${currentPageClient eq i}">
                                            <span class="page-link active">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/admin/users?pageAdmin=${currentPageAdmin}&pageClient=${i}" class="page-link">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <c:if test="${currentPageClient < noOfPagesClient}">
                                    <a href="${pageContext.request.contextPath}/admin/users?pageAdmin=${currentPageAdmin}&pageClient=${currentPageClient + 1}" class="page-link">&raquo;</a>
                                </c:if>
                            </div>
                        </c:if>
                    </c:if>
                    <c:if test="${empty clientUsers}">
                        <div class="no-data-card">Nessun cliente in questa pagina.</div>
                    </c:if>
                </div>
            </div>
        </div>
    </body>