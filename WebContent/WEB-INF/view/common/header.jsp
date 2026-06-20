<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common-head.jsp" %>
<title></title>
</head>
<body>
	<header class="top-section">
        <div class="div-logo">
            <a href="${pageContext.request.contextPath}/index.jsp">
                <img src="${pageContext.request.contextPath}/images/assets/logo.png" alt="LOGO">
            </a>
        </div>
        
        <div class="div-search">
            <form action="RicercaServlet" method="GET" class="form-search">
                <input type="text" name="query" id="search" placeholder="Cerca prodotto" required>
                <button type="submit">Cerca</button>
            </form>
            <div id="search-suggestions"></div>
        </div>
        
        <div class="div-actions">
            <a href="" class="action-element">
                <div class="action-picture">
                    <img src="${pageContext.request.contextPath}/images/assets/action1.png" alt="ACT1">
                </div>
                <span>Action1</span>
            </a>
            
            <a href="" class="action-element">
                <div class="action-picture">
                    <img src="${pageContext.request.contextPath}/images/assets/action2.png" alt="ACT2">
                </div>
                <span>Action2</span>
            </a>
            
            <a href="" class="action-element">
                <div class="action-picture">
                    <img src="${pageContext.request.contextPath}/images/assets/action3.png" alt="ACT3">
                </div>
                <span>Action3</span>
            </a>
        </div>
    </header>
</body>
</html>