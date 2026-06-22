<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<% String feedback = (String) request.getAttribute("feedback"); %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common-head.jsp" %>
	<title>Il mio account</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile/profile-tabs.css">
</head>
<body>
	<%@ include file="header.jsp" %>
		<section class="side-page">
			<div class="tabs">
				<div class="header">
					<h3>Il mio account</h3>
				</div>
				<ul id="tabs-item" data-active-tab="${activeTab} }">
					<li class="${activeTab == 'personal-data' ? 'active' : ''}" data-tab="personal-data"><a href="${pageContext.request.contextPath}/profile/personal-data">Dati personali</a></li>
					<li class="${activeTab == 'addresses' ? 'active' : ''}" data-tab="addresses"><a href="${pageContext.request.contextPath}/profile/addresses">Indirizzi di spedizione</a></li>
					<li class="${activeTab == 'orders-history' ? 'active' : ''}" data-tab="orders-history"><a href="${pageContext.request.contextPath}/profile/orders-history">I miei ordini</a></li>
					<li class="${activeTab == 'reviews' ? 'active' : ''}" data-tab="reviews"><a href="${pageContext.request.contextPath}/profile/reviews">Le mie recensioni</a></li>
					<li class="logout-item"><a href="../logout">Esci dall'account</a></li>
				</ul>
			</div>
		</section>
		<section class="tab-content">
		<% if (feedback != null) { %>
        	<div class="feedback-div"><%= feedback %></div>
    	<% } %>
 		<jsp:include page="${contentPage}"/>
		</section>
	<%@ include file="footer.jsp" %>
	<script src="${pageContext.request.contextPath}/scripts/profile/profile-tabs.js"></script>
</body>
</html>