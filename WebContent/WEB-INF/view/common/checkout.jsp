<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    boolean isLoggato = (session.getAttribute("utenteLoggato") != null);
%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="common-head.jsp" %>
    <title>Cassa - Zampastico</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <jsp:include page="header.jsp" />

    <main class="checkout-container container">
        
        <% if (!isLoggato) { %>
            
            <div class="auth-card auth-lock-card">
                <i class="fa-solid fa-lock lock-icon"></i>
                <h2>Accesso Richiesto</h2>
                <p>Ehi! Sembra che tu non sia ancora entrato nel branco. <br>Devi effettuare l'accesso per poter completare l'acquisto in modo sicuro.</p>
                <a href="${pageContext.request.contextPath}/login" class="btn-primary btn-login-prompt">Vai al Login</a>
            </div>
            
        <% } else { %>
            
            <div class="checkout-header">
                <h1>Concludi l'ordine <i class="fa-solid fa-box-open primary-icon"></i></h1>
            </div>

            <div class="checkout-layout">
                <div class="checkout-form-section">
                    <form id="formCheckout" action="${pageContext.request.contextPath}/processaOrdine" method="POST">
                        
                        <div class="checkout-box">
                            <h3><i class="fa-solid fa-truck-fast"></i> Indirizzo di Spedizione</h3>
                            <div class="form-group">
                                <label for="via">Via e Numero Civico *</label>
                                <input type="text" id="via" name="via" class="form-control" required>
                                <span id="err-via" class="error-msg"></span>
                            </div>
                            
                            <div class="form-row">
                                <div class="form-group col-2">
                                    <label for="citta">Città *</label>
                                    <input type="text" id="citta" name="citta" class="form-control" required>
                                    <span id="err-citta" class="error-msg"></span>
                                </div>
                                <div class="form-group col-1">
                                    <label for="cap">CAP *</label>
                                    <input type="text" id="cap" name="cap" class="form-control" maxlength="5" required>
                                    <span id="err-cap" class="error-msg"></span>
                                </div>
                            </div>
                        </div>

                        <div class="checkout-box">
                            <h3><i class="fa-solid fa-credit-card"></i> Metodo di Pagamento</h3>
                            <div class="payment-methods">
                                <label class="payment-option">
                                    <input type="radio" name="metodo_pagamento" value="carta_credito" checked>
                                    <span><i class="fa-brands fa-cc-visa"></i> Carta di Credito</span>
                                </label>
                                <label class="payment-option">
                                    <input type="radio" name="metodo_pagamento" value="paypal">
                                    <span><i class="fa-brands fa-paypal"></i> PayPal</span>
                                </label>
                            </div>
                        </div>

                        <button type="submit" class="btn-primary btn-block btn-checkout"><i class="fa-solid fa-lock"></i> Paga e Conferma Ordine</button>
                    </form>
                </div>

                <div class="checkout-summary-section">
                    <div class="checkout-box">
                        <h3>Riepilogo Ordine</h3>
                        
                        <div id="checkout-items-container">
                            <p class="loading-cart-msg">Caricamento carrello...</p>
                        </div>
                        
                        <div class="coupon-section">
							    <label for="couponCode">Hai un codice sconto?</label>
							    <div class="coupon-input-group">
							        <input type="text" id="couponCode" placeholder="ES. ZAMPA10" autocomplete="off">
							        <button type="button" id="btn-apply-coupon" class="btn-secondary">Applica</button>
							    </div>
							    <span id="coupon-msg" class="coupon-message"></span>
							</div>
                        <hr class="checkout-divider">
                        
                        <div class="summary-row total-row">
                            <span>Totale da Pagare:</span>
                            <span id="checkout-total-price">0,00€</span>
                        </div>
                    </div>
                </div>
            </div>
        <% } %>
    </main>

    <jsp:include page="footer.jsp" />
    <script src="${pageContext.request.contextPath}/scripts/main.js"></script>
</body>
</html>