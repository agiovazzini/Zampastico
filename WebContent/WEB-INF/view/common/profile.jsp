<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<% String feedback = (String) request.getAttribute("feedback"); %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common-head.jsp" %>
	<title>Il mio account</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile/profile.css">
</head>
<body>
	<%@ include file="header.jsp" %>
		<section class="side-page">
			<div class="tabs">
				<div class="header">
					<h3>Il mio account</h3>
				</div>
				<ul class="tabs-item">
					<li><a href="">Dati personali</a></li>
					<li><a href="">Indirizzi di spedizione</a></li>
					<li><a href="">I miei ordini</a></li>
					<li><a href="">Le mie recensioni</a></li>
					<li class="logout-item"><a href="">Esci dall'account</a></li>
				</ul>
			</div>
		</section>
		<section class="tab-content">
		<% if (feedback != null) { %>
        	<div class="feedback-div"><%= feedback %></div>
    	<% } %>
 		<!-- CONTENUTO DEL TAB -->
		</section>
	<%@ include file="footer.jsp" %>
	<script src="${pageContext.request.contextPath}/scripts/profile/profile.js"></script>
</body>
</html>