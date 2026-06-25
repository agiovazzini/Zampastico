<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/profile/my-address.css">
    </head>
    <body>
        <div class="addresses-wrapper">
            <c:set var="isLocked" value="${addresses.size() >= 5}" />
            <div class="address-container ${isLocked ? 'locked-mode' : ''}">
                <div class="locked-overlay">
                    <div class="locked-message"> 🔒 Limite Raggiunto <span>Hai raggiunto 5 indirizzi. Modificane o eliminane uno per aggiungerne di nuovi.</span>
                    </div>
                </div>
                <h3>Aggiungi un nuovo indirizzo</h3>
                <form class="address-form" id="address-settings" action="${pageContext.request.contextPath}/addAddress" method="POST">
                    <div class="form-element">
                        <label for="addressStreet">Via</label>
                        <input type="text" id="addressStreet" name="streetForm" value="" required ${isLocked ? 'disabled' : '' }>
                    </div>
                    <div class="form-element">
                        <label for="addressHouseNumber">Civico</label>
                        <input type="text" id="addressHouseNumber" name="houseNumberForm" value="" required ${isLocked ? 'disabled' : '' }>
                    </div>
                    <div class="form-element">
                        <label for="addressCity">Città</label>
                        <input type="text" id="addressCity" name="cityForm" value="" required ${isLocked ? 'disabled' : '' }>
                    </div>
                    <div class="form-element">
                        <label for="addressProvince">Provincia</label>
                        <input type="text" id="addressProvince" name="provinceForm" value="" required ${isLocked ? 'disabled' : '' }>
                    </div>
                    <div class="form-element">
                        <label for="addressPostCode">CAP</label>
                        <input type="text" id="addressPostCode" name="postCodeForm" value="" required ${isLocked ? 'disabled' : '' }>
                    </div>
                    <div class="form-element" id="defaultForm">
                        <label for="addressDefault">Vuoi scegliere questo indirizzo come predefinito?</label>
                        <input type="checkbox" id="addressDefault" name="defaultForm" value="true" ${isLocked ? 'disabled' : '' }>
                    </div>
                    <input type="hidden" id="idAddress" name="idAddress" value="">
                    <button type="button" class="saveButton" onclick="submitAddress()" ${isLocked ? 'disabled' : '' }>Salva indirizzo</button>
                    <button type="button" id="cancelEditButton" class="deleteButton" style="display: none" onclick="resetForm()">Annulla</button>
                </form>
            </div>
            <c:if test="${not empty addresses}">
                <div class="addresses-list">
                    <h3>I tuoi indirizzi salvati</h3>
                    <c:forEach var="address" items="${addresses}">
                        <div class="address-element ${address.predefinito ? 'isDefault' : ''}">
                            <c:if test="${address.predefinito}">
                                <h4>Indirizzo Predefinito</h4>
                            </c:if>
                            <p>${address.via}, ${address.citta} (${address.provincia}) - ${address.cap}</p>
                            <div class="button-group">
                                <form action="${pageContext.request.contextPath}/deleteAddress" method="POST">
                                    <input type="hidden" name="idAddress" value="${address.idIndirizzo}">
                                    <button type="submit" class="deleteButton">Elimina</button>
                                </form>
                                <button type="button" class="editButton" data-id="${address.idIndirizzo}" data-via="${address.via}" data-citta="${address.citta}" data-provincia="${address.provincia}" data-cap="${address.cap}" data-default="${address.predefinito}" onclick="editAddress(this)">Modifica</button>
                                <c:if test="${not address.predefinito}">
                                    <form action="${pageContext.request.contextPath}/setDefaultAddress" method="POST">
                                        <input type="hidden" name="idAddress" value="${address.idIndirizzo}">
                                        <button type="submit" class="defaultButton">Predefinito</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${empty addresses}">
                <div class="addresses-list">
                    <h3>I tuoi indirizzi salvati</h3>
                    <div class="no-addresses">
                        <p>Non hai ancora salvato alcun indirizzo su Zampastico.</p>
                    </div>
                </div>
            </c:if>
        </div>
        <script src="${pageContext.request.contextPath}/scripts/profile/my-address.js"></script>
    </body>
</html>