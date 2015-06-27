<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Kontakt</title>
		
		<style type="text/css">
		.greska {
		   font-family: fantasy;
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF0000;
		}
		</style>
	</head>

	<body>
		<h1>
		<c:choose>
		<c:when test="${zapis.id.isEmpty()}">
		Novi kontakt
		</c:when>
		<c:otherwise>
		Uređivanje kontakta
		</c:otherwise>
		</c:choose>
		</h1>

		<form action="save" method="post">
		
		ID <input type="text" name="id" value='<c:out value="${zapis.id}"/>' size="5"><br>
		<c:if test="${zapis.imaPogresku('id')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('id')}"/></div>
		</c:if>
		
		Ime <input type="text" name="ime" value='<c:out value="${zapis.ime}"/>' size="30"><br>
		<c:if test="${zapis.imaPogresku('ime')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('ime')}"/></div>
		</c:if>

		Prezime <input type="text" name="prezime" value='<c:out value="${zapis.prezime}"/>' size="50"><br>
		<c:if test="${zapis.imaPogresku('prezime')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('prezime')}"/></div>
		</c:if>

		EMail <input type="text" name="email" value='<c:out value="${zapis.email}"/>' size="100"><br>
		<c:if test="${zapis.imaPogresku('email')}">
		<div class="greska"><c:out value="${zapis.dohvatiPogresku('email')}"/></div>
		</c:if>

		<input type="submit" name="metoda" value="Pohrani">
		<input type="submit" name="metoda" value="Odustani">
		
		</form>













	</body>
</html>