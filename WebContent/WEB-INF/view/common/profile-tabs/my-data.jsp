<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
	<div class="data-container">
		<form class="data-form" id="profile-settings" action="" method="POST">
			<div class="form-element">
				<label for="name">Nome</label>
				<input type="text" id="name" name="nameForm" value="" required>
			</div>
			<div class="form-element">
				<label for="surname">Cognome</label>
				<input type="text" id="surname" name="surnameForm" value="" required>
			</div>
			<button type="button" class="saveButton" onclick="">Salva modifiche</button>
		</form>
	</div>
	<div class="password-container">
		<form class="data-form" id="password-settings" action="" method="POST">
			<div class="form-element">
				<label for="currentPassword">Password Attuale</label>
				<input type="text" id="password" name="passwordForm" value="" required>
			</div>
			<div class="form-element">
				<label for="newPassword">Nuova Password</label>
				<input type="text" id="newPassword" name="newPasswordForm" value="" required>
			</div>
			<div class="form-element">
				<label for="confirmPassword">Conferma Password</label>
				<input type="text" id="confirmPassword" name="confirmPasswordForm" value="" required>
			</div>
			<button type="button" class="saveButton" onclick="">Aggiorna Password</button>
		</form>
	</div>
	<div class="anonym-container">
			<div class="form-element">
				<label>Vuoi applicare il tuo diritto all'oblio?</label>
				<button type="button" class="saveButton" onclick="">Cancella l'account</button>
			</div>
	</div>
</body>
</html>