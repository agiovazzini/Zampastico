<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="common-head.jsp" %>
	<title>Zampastico Footer</title>
	<p></p>
</head>
<body>
	<footer class="bottom-section">
		<div class="footer-container">
			<div class="footer-main">
				<div class="footer-brand">
					<h3>ZAMPASTICO</h3>
					<p class="brand-description">Qualità e amore per i tuoi amici a quattro zampe, ogni giorno.</p>
				</div>
				<div class="footer-columns">
					<div class="footer-col">
						<h4>Assistenza</h4>
						<ul>
							<li><a href="#">Contattaci</a></li>
							<li><a href="#">FAQ</a></li>
							<li><a href="#">Spedizioni e resi</a></li>
						</ul>
					</div>
					<div class="footer-col">
						<h4>Informazioni</h4>
						<ul>
							<li><a href="#">Termini e condizioni</a></li>
							<li><a href="#">Privacy Policy</a></li>
							<li><a href="#">Cookie Policy</a></li>
							<li><a href="#">Lavora con noi</a></li>
						</ul>
					</div>
					<div class="footer-col">
						<h4>Pagamenti</h4>
						<div class="payment-methods">
							<img src="${pageContext.request.contextPath}/images/assets/icons/visa.svg" alt="Visa" height="32" width="auto">
							<img src="${pageContext.request.contextPath}/images/assets/icons/mastercard.svg" alt="Mastercard" height="32" width="auto">
							<img src="${pageContext.request.contextPath}/images/assets/icons/paypal.svg" alt="PayPal" height="32" width="auto">
							<img src="${pageContext.request.contextPath}/images/assets/icons/apple-pay.svg" alt="Apple Pay" height="32" width="auto">
						</div>
					</div>
				</div>
			</div>
			<hr class="footer-divider">
			<div class="footer-bottom">
				<div class="copyright">
					Zampastico © 2026 – Tutti i diritti riservati.
				</div>
				
				<div class="footer-socials">
					<a href="#"><i class="fa-brands fa-facebook-f"></i></a>
					<a href="#"><i class="fa-brands fa-instagram"></i></a>
					<a href="#"><i class="fa-brands fa-x-twitter"></i></a>
					<a href="#"><i class="fa-brands fa-youtube"></i></a>
					<a href="#"><i class="fa-brands fa-tiktok"></i></a>
				</div>
				<div class="back-to-top">
					<a href="#"><i class="fa-solid fa-chevron-up"></i></a>
				</div>
			</div>
			
		</div>
	</footer>
</body>
</html>