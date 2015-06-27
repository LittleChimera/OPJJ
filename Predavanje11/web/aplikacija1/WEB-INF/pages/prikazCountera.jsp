<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Azuriranje brojaca</title>
</head>
<body>
	<h1>Trenutna vrijednost brojaca</h1>
	<p>Trenutna vrijednost brojaca je ${counter}.</p>

	<form action="azuriraj" method="post">
		Parameter A: <input type="text" value="0" name="a"><br>
		Parameter B: <input type="text" value="0" name="b"><br> 
		<input type="submit" value="Posalji">
		<input type="reset" value="Ponisti">
	</form>
</body>
</html>