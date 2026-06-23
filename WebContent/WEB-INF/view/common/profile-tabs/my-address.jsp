<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
	<div class="address-container">
		<form class="address-form" id="address-settings" action="${pageContext.request.contextPath}/" method="POST">
			<div class="form-element">
				<label for="addressStreet">Via</label>
				<input type="text" id="addressStreet" name="streetForm" value="" required>
			</div>
			<div class="form-element">
				<label for="addressHouseNumber">Civico</label>
				<input type="text" id="addressHouseNumber" name="houseNumberForm" value="" required>
			</div>
			<div class="form-element">
				<label for="addressCity">Città</label>
				<input type="text" id="addressCity" name="cityForm" value="" required>
			</div>
			<div class="form-element">
				<label for="addressProvince">Provincia</label>
				<input type="text" id="addressProvince" name="provinceForm" value="" required>
			</div>
			<div class="form-element">
				<label for="addressPostCode">CAP</label>
				<input type="text" id="addressPostCode" name="postCodeForm" value="" required>
			</div>
			<div class="form-element" id="defaultForm">
				<label for="addressDefault">Vuoi scegliere questo indirizzo come predefinito?</label>
				<input type="checkbox" id="addressDefault" name="defaultForm" value="true">
			</div>
			<button type="button" class="saveButton" onclick="">Salva indirizzo</button>
		</form>
	</div>
</body>
</html>