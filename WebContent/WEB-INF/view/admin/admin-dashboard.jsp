<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="jakarta.tags.core" prefix="c" %><% String feedback = (String) session.getAttribute("feedback"); %>
<!DOCTYPE html>
<html>
    <head><%@ include file="../common/common-head.jsp" %> <title>Admin Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile/profile.css">
    </head>
    <body><%@ include file="../common/header.jsp" %> <div class="profile-container">
            <section class="side-page">
                <div class="tabs">
                    <div class="header">
                        <h3>Admin Dashboard</h3>
                    </div>
                    <ul id="tabs-item" data-active-tab="${activeTab}">
                        <li class="${activeTab == 'users' ? 'active' : 'inactive'}" data-tab="users">
                            <a href="${pageContext.request.contextPath}/admin/users">Gestione utenti</a>
                        </li>
                        <li class="${activeTab == 'orders' ? 'active' : 'inactive'}" data-tab="orders">
                            <a href="${pageContext.request.contextPath}/admin/orders">Pannello ordini</a>
                        </li>
                        <li class="${activeTab == 'coupons' ? 'active' : 'inactive'}" data-tab="coupons">
                            <a href="${pageContext.request.contextPath}/admin/coupons">Gestione coupons</a>
                        </li>
                        <li class="${activeTab == 'product' ? 'active' : 'inactive'}" data-tab="product">
                            <a href="${pageContext.request.contextPath}/admin/product">Gestione prodotti</a>
                        </li>
                        <li class="logout-item">
                            <a href="${pageContext.request.contextPath}/logout">Esci dall'account</a>
                        </li>
                    </ul>
                </div>
            </section>
            <section class="tab-content"><% if (feedback != null) { %> <div class="feedback-div"><%= feedback %> </div><% } %><% if (feedback != null) session.removeAttribute("feedback");%> <c:if test="${not empty contentPage}">
                    <jsp:include page="${contentPage}" />
                </c:if>
            </section>
        </div><%@ include file="../common/footer.jsp" %> <script src="${pageContext.request.contextPath}/scripts/profile/profile-tabs.js"></script>
    </body>
</html>