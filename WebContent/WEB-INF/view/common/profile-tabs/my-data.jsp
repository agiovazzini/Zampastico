<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<body>
	<div class="data-container">
		<form class="data-form" id="profile-settings" action="${pageContext.request.contextPath}/updateData" method="POST">
			<div class="form-element">
				<label for="name">Nome</label>
				<input type="text" id="name" name="nameForm" value="<c:out value='${utenteLoggato.nome}'/>" required>
			</div>
			<div class="form-element">
				<label for="surname">Cognome</label>
				<input type="text" id="surname" name="surnameForm" value="<c:out value='${utenteLoggato.cognome}'/>" required>
			</div>
			<button type="button" class="saveButton" onclick="submitUpdate()">Salva modifiche</button>
		</form>
	</div>
	<div class="password-container">
		<form class="data-form" id="password-settings" action="${pageContext.request.contextPath}/updatePassword" method="POST">
			<div class="form-element">
				<label for="currentPassword">Password Attuale</label>
				<input type="password" id="currentPassword" name="passwordForm" value="" required>
			</div>
			<div class="form-element">
				<label for="newPassword">Nuova Password</label>
				<input type="password" id="newPassword" name="newPasswordForm" value="" required>
			</div>
			<div class="form-element">
				<label for="confirmPassword">Conferma Password</label>
				<input type="password" id="confirmPassword" name="confirmPasswordForm" value="" required>
			</div>
			<button type="button" class="saveButton" onclick="submitPassword()">Aggiorna Password</button>
		</form>
	</div>
	<div class="anonym-container">
		<form id="delete-form" action="${pageContext.request.contextPath}/deleteAccount" method="POST">
			<div class="form-element">
				<label>Vuoi applicare il tuo diritto all'oblio?</label>
				<button type="button" class="saveButton" onclick="submitDeletion()">Cancella l'account</button>
			</div>
		</form>
	</div>
	<script src="${pageContext.request.contextPath}/scripts/profile/personal-data.js"></script>
</body>
</html>